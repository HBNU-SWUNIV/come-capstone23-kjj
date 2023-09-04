package com.hanbat.zanbanzero.controller.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.user.TokenRefreshDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.external.KeycloakProperties;
import com.hanbat.zanbanzero.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/user/login/")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserService userService;
    private final KeycloakProperties keycloakProperties;

    @Operation(summary="로그인", description="username과 password를 입력받아 로그인 시도")
    @PostMapping("login/id")
    public ResponseEntity<UserInfoDto> userLogin(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(userService.getInfoForUsername(username));
    }

    @Operation(summary="Keycloak 로그인 페이지")
    @GetMapping("login/keycloak/page")
    public RedirectView redirectKeycloakLoginPage() {
        RedirectView redirectView = new RedirectView();
        String uri = keycloakProperties.getHost() +
                "/auth/realms/" +
                keycloakProperties.getRealmName() +
                "/protocol/openid-connect/auth" +
                "?response_type=code" +
                "&client_id=" + keycloakProperties.getClientId() +
                "&scope=profile email roles openid" +
                "&redirect_uri=" + keycloakProperties.getRedirectUri();
        redirectView.setUrl(uri);
        return redirectView;
    }

    @GetMapping("login/keycloak/redirect")
    public ResponseEntity<String> redirectAfterKeycloakLoginPage() {
        return ResponseEntity.ok("keycloak login success");
    }

    @Operation(summary="Keycloak 로그인")
    @Parameters({
            @Parameter(name = "token", description = "Keycloak에서 발급받은 token", required = true, in = ParameterIn.QUERY)})
    @PostMapping("login/keycloak")
    public ResponseEntity<UserInfoDto> userLoginFromKeycloak(HttpServletRequest request) throws JsonProcessingException {
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(userService.loginFromKeycloak(user));
    }

    @Operation(summary="Access Token 재발급", description = "request header에 Refresh token 첨부 필요")
    @PostMapping("login/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody TokenRefreshDto dto) {
        userService.refreshToken(request, response, dto);
        return ResponseEntity.ok("refresh success");
    }

    @Operation(summary="회원가입", description="username과 password를 입력받아 회원가입 시도")
    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody UserJoinDto dto) throws WrongRequestDetails {
        if (!dto.checkForm()) throw new WrongRequestDetails("잘못된 정보입니다.");
        userService.join(dto);

        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    @Operation(summary="아이디 중복 체크", description="username만 입력받아 중복체크")
    @GetMapping("join/check")
    public ResponseEntity<Boolean> check(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.check(username));
    }
}
