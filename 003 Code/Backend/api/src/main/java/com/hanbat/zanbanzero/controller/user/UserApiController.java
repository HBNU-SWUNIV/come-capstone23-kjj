package com.hanbat.zanbanzero.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.user.LoginDto;
import com.hanbat.zanbanzero.dto.user.TokenRefreshDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.*;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
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

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class UserApiController {

    private final UserService userService;
    private final KeycloakProperties keycloakProperties;

    @Operation(summary="로그인", description="username과 password를 입력받아 로그인 시도")
    @PostMapping("login/id")
    public ResponseEntity<UserInfoDto> userLogin(HttpServletRequest request, @RequestBody LoginDto loginDto) {
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

    @Operation(summary="Access Token 재발급", description = "request body에 Refresh token 첨부 필요")
    @PostMapping("login/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody TokenRefreshDto dto) {
        userService.refreshToken(request, response, dto);
        return ResponseEntity.ok("refresh success");
    }

    @Operation(summary="회원가입", description="username과 password를 입력받아 회원가입 시도")
    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody UserJoinDto dto) throws JsonProcessingException, WrongRequestDetails {
        if (!dto.checkForm()) throw new WrongRequestDetails("잘못된 정보입니다.");
        userService.join(dto);

        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    @Operation(summary="회원탈퇴", description="username과 password를 입력받아 회원탈퇴")
    @DeleteMapping("withdraw")
    public ResponseEntity<String> withdraw(@RequestBody UserJoinDto dto) throws WrongRequestDetails {
        if (!dto.checkForm()) throw new WrongRequestDetails("잘못된 정보입니다.");
        userService.withdraw(dto);

        return ResponseEntity.ok("탈퇴되었습니다.");
    }

    @Operation(summary="아이디 중복 체크", description="username만 입력받아 중복체크")
    @GetMapping("join/check")
    public ResponseEntity<Boolean> check(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.check(username));
    }

    @Operation(summary="일반회원 대표정보 조회")
    @GetMapping("info")
    public ResponseEntity<UserInfoDto> getInfo(HttpServletRequest request) throws CantFindByIdException {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));

        return ResponseEntity.ok(userService.getInfo(username));
    }

    @Operation(summary="일반 유저 마이페이지 조회", description="유저 상세정보 조회")
    @GetMapping("page")
    public ResponseEntity<UserMypageDto> getMyPage(HttpServletRequest request) throws CantFindByIdException, JsonProcessingException {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));

        return ResponseEntity.ok(userService.getMyPage(username));
    }

    @Operation(summary="일반 유저 요일정책 설정", description="유저 요일정책 설정")
    @PatchMapping("policy/date")
    public ResponseEntity<UserPolicyDto> setUserDatePolicy(HttpServletRequest request, @RequestBody UserPolicyDto dto) throws CantFindByIdException {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        return ResponseEntity.ok(userService.setUserDatePolicy(dto, username));
    }

    @Operation(summary="일반 유저 메뉴정책 설정", description="유저 메뉴정책 설정")
    @PatchMapping("policy/menu/{menu_id}")
    public ResponseEntity<UserPolicyDto> setUserMenuPolicy(HttpServletRequest request, @PathVariable Long menu_id) throws CantFindByIdException, WrongParameter {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));

        return ResponseEntity.ok(userService.setUserMenuPolicy(username, menu_id));
    }

    @Operation(summary="일반 유저 정책 조회", description="유저 요일정책 조회")
    @GetMapping("policy/date")
    public ResponseEntity<UserPolicyDto> getUserPolicy(HttpServletRequest request) throws CantFindByIdException {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));

        return ResponseEntity.ok(userService.getUserPolicy(username));
    }

    @GetMapping("login/test")
    public ResponseEntity<Map<String, String>> testToken(){
        return ResponseEntity.ok(userService.testToken());
    }
}