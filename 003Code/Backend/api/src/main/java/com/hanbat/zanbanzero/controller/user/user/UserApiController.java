package com.hanbat.zanbanzero.controller.user.user;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.user.auth.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.user.sso.UserSsoService;
import com.hanbat.zanbanzero.service.user.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestControllerClass("/api/user")
public class UserApiController {

    private final UserService userService;
    private final UserSsoService userSsoService;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate = new JwtTemplate();

    /**
     * 회원 탈퇴
     *
     * @param dto - password
     * @return "탈퇴되었습니다."
     * @throws WrongRequestDetails - 비밀번호가 맞지 않을 경우 발생
     */
    @Operation(summary="회원탈퇴")
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(HttpServletRequest request, @RequestBody WithdrawDto dto) throws WrongRequestDetails, CantFindByIdException, CantFindByUsernameException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));
        userService.withdraw(username, dto);

        return ResponseEntity.ok("탈퇴되었습니다.");
    }

    @Operation(summary="Keycloak 회원탈퇴")
    @DeleteMapping("/withdraw/keycloak")
    public ResponseEntity<String> withdrawKeycloak(HttpServletRequest request) throws CantFindByUsernameException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));
        userSsoService.withdrawSso(username);
        return ResponseEntity.ok("Keyclaok 탈퇴되었습니다.");
    }

    /**
     * 일반 유저 대표정보(id, username) 조회
     *
     * @return UserInfoDto
     * @throws CantFindByIdException - user가 없을 경우 발생
     */
    @Operation(summary="일반회원 대표정보 조회")
    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> getInfo(HttpServletRequest request) throws CantFindByIdException {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getInfo(id));
    }
}