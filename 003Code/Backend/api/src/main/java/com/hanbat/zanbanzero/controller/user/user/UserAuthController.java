package com.hanbat.zanbanzero.controller.user.user;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.KeycloakJoinException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.user.user.UserService;
import com.hanbat.zanbanzero.service.user.sso.UserSsoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerClass("/api/user/login/")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserService userService;
    private final UserSsoService userSsoService;

    /**
     * username, password로 로그인
     * response header에 토큰 발급
     *
     * @return UserInfoDto
     */
    @Operation(summary="로그인", description="username과 password를 입력받아 로그인 시도")
    @PostMapping("id")
    public ResponseEntity<UserInfoDto> userLogin(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(userService.getInfoForUsername(username));
    }

    @Operation(summary="Keycloak 회원가입")
    @PostMapping("join/keycloak")
    public ResponseEntity<String> joinKeycloak(@RequestBody UserJoinDto dto) {
        if (userSsoService.existsByUsername(dto.getUsername())) throw new KeycloakJoinException("username already exists - username : " + dto.getUsername());
        userSsoService.joinSso(dto);
        return ResponseEntity.ok("join success");
    }

    /**
     * 클라이언트가 보낸 token으로 유저 인증
     *
     * @return UserInfoDto
     */
    @Operation(summary="Keycloak 로그인")
    @Parameter(name = "token", description = "Keycloak에서 발급받은 token", required = true, in = ParameterIn.QUERY)
    @PostMapping("keycloak")
    public ResponseEntity<UserInfoDto> userLoginFromKeycloak(HttpServletRequest request) throws CantFindByIdException {
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(userSsoService.login(user.getId()));
    }

    /**
     * 토큰 재발급 로직
     * response header에 access, refresh 모두 재발급
     */
    @Operation(summary="Access Token 재발급", description = "request header에 Refresh token 첨부 필요")
    @PostMapping("refresh")
    public ResponseEntity<String> refreshToken() {
        return ResponseEntity.ok("refresh success");
    }

    /**
     * 회원가입
     *
     * @param dto - username, password
     * @throws WrongRequestDetails - username, password 둘 중 하나라도 null일 때 발생
     */
    @Operation(summary="회원가입", description="username과 password를 입력받아 회원가입 시도")
    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody UserJoinDto dto) throws WrongRequestDetails {
        if (!dto.checkForm()) throw new WrongRequestDetails("dto : " + dto);
        userService.join(dto);

        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    /**
     * 아이디 중복 체크
     *
     * @param username - 로그인 아이디
     * @return Boolean
     */
    @Operation(summary="아이디 중복 체크", description="username만 입력받아 중복체크")
    @GetMapping("join/check")
    public ResponseEntity<Boolean> check(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.check(username));
    }
}
