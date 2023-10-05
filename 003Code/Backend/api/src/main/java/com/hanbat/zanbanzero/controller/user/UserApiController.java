package com.hanbat.zanbanzero.controller.user;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.user.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UsePointDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class UserApiController {

    private final UserService userService;
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
    @PostMapping("withdraw")
    public ResponseEntity<String> withdraw(HttpServletRequest request, @RequestBody WithdrawDto dto) throws WrongRequestDetails, CantFindByIdException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));
        userService.withdraw(username, dto);

        return ResponseEntity.ok("탈퇴되었습니다.");
    }

    @Operation(summary="Keycloak 회원탈퇴")
    @DeleteMapping("withdraw/keycloak")
    public ResponseEntity<String> withdrawKeycloak(HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));
        userService.withdrawKeycloak(username);
        return ResponseEntity.ok("Keyclaok 탈퇴되었습니다.");
    }

    /**
     * 일반 유저 대표정보(id, username) 조회
     *
     * @return UserInfoDto
     * @throws CantFindByIdException - user가 없을 경우 발생
     */
    @Operation(summary="일반회원 대표정보 조회")
    @GetMapping("info")
    public ResponseEntity<UserInfoDto> getInfo(HttpServletRequest request) throws CantFindByIdException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getInfo(username));
    }

    /**
     * 일반 유저 마이페이지 조회
     * userId, point
     *
     * @return UserMypageDto
     * @throws CantFindByIdException - userMyPage가 없을 경우 발생
     */
    @Operation(summary="일반 유저 마이페이지 조회", description="유저 상세정보 조회")
    @GetMapping("page")
    public ResponseEntity<UserMypageDto> getMyPage(HttpServletRequest request) throws CantFindByIdException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getMyPage(username));
    }

    @Operation(summary="포인트 사용")
    @PostMapping("page/point")
    public ResponseEntity<Integer> usePoint(HttpServletRequest request, @RequestBody UsePointDto dto) throws CantFindByIdException {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.usePoint(id, dto));
    }

    /**
     * 일반 유저 요일 정책 설정
     *
     * @param dto - monday ~ friday bool값 / defaultMenu menuID
     * @return UserPolicyDto
     * @throws CantFindByIdException - userPolicy가 없을 경우 발생
     */
    @Operation(summary="일반 유저 요일정책 설정", description="유저 요일정책 설정")
    @PatchMapping("policy/date")
    public ResponseEntity<UserPolicyDto> setUserDatePolicy(HttpServletRequest request, @RequestBody UserPolicyDto dto) throws CantFindByIdException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));
        return ResponseEntity.ok(userService.setUserDatePolicy(dto, username));
    }

    /**
     * 일반 유저 메뉴 정책 설정
     *
     * @param menuId - 설정할 메뉴 ID
     * @return UserPolicyDto
     * @throws CantFindByIdException - UserPolicy가 없을 경우 발생
     * @throws WrongParameter - 메뉴가 없을 경우 발생
     */
    @Operation(summary="일반 유저 메뉴정책 설정", description="유저 메뉴정책 설정")
    @PatchMapping("policy/menu/{menuId}")
    public ResponseEntity<UserPolicyDto> setUserMenuPolicy(HttpServletRequest request, @PathVariable Long menuId) throws CantFindByIdException, WrongParameter {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.setUserMenuPolicy(username, menuId));
    }

    /**
     * 일반 유저 정책 조회
     *
     * @param request
     * @return
     * @throws CantFindByIdException
     */
    @Operation(summary="일반 유저 정책 조회", description="유저 요일정책 조회")
    @GetMapping("policy/date")
    public ResponseEntity<UserPolicyDto> getUserPolicy(HttpServletRequest request) throws CantFindByIdException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getUserPolicy(username));
    }

    @GetMapping("login/test")
    public ResponseEntity<Map<String, String>> testToken(@RequestParam("username") String username){
        return ResponseEntity.ok(userService.testToken(username));
    }
}