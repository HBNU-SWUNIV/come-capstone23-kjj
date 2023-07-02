package com.hanbat.zanbanzero.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.*;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.exception.controller.exceptions.*;
import com.hanbat.zanbanzero.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class UserApiController {

    private final UserService userService;

    @Operation(summary="로그인", description="username과 password를 입력받아 로그인 시도")
    @PostMapping("login/id")
    public ResponseEntity<UserInfoDto> userLogin(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getInfoForUsername(username));
    }

    @Operation(summary="Keycloak 로그인", description="username과 password를 입력받아 로그인 시도")
    @PostMapping("login/keycloak")
    public ResponseEntity<String> userLoginFromKeycloak(HttpServletRequest request) throws JsonProcessingException {
        User user = (User) request.getAttribute("user");
        userService.loginFromKeycloak(user);
        return ResponseEntity.status(HttpStatus.OK).body("로그인 되었습니다.");
    }

    @Operation(summary="회원가입", description="username과 password를 입력받아 회원가입 시도")
    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody UserJoinDto dto) throws JsonProcessingException, WrongRequestDetails {
        if (!dto.checkForm()) throw new WrongRequestDetails("잘못된 정보입니다.");
        userService.join(dto);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    @Operation(summary="회원탈퇴", description="username과 password를 입력받아 회원탈퇴")
    @DeleteMapping("withdraw")
    public ResponseEntity<String> withdraw(@RequestBody UserJoinDto dto) throws WrongRequestDetails {
        if (!dto.checkForm()) throw new WrongRequestDetails("잘못된 정보입니다.");
        userService.withdraw(dto);

        return ResponseEntity.status(HttpStatus.OK).body("탈퇴되었습니다.");
    }

    @Operation(summary="아이디 중복 체크", description="username만 입력받아 중복체크")
    @GetMapping("join/check")
    public ResponseEntity<Boolean> check(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.check(username));
    }

    @Operation(summary="일반회원 대표정보 조회")
    @GetMapping("info")
    public ResponseEntity<UserInfoDto> getInfo(@RequestParam("username") String username) throws CantFindByIdException {
        return ResponseEntity.ok(userService.getInfo(username));
    }

    @Operation(summary="일반 유저 마이페이지 조회", description="유저 상세정보 조회")
    @GetMapping("{id}/page")
    public ResponseEntity<UserMypageDto> getMyPage(@PathVariable Long id) throws CantFindByIdException, JsonProcessingException {
        UserMypageDto userMyPageDto = userService.getMyPage(id);

        return ResponseEntity.status(HttpStatus.OK).body(userMyPageDto);
    }

    @Operation(summary="일반 유저 요일정책 설정", description="유저 요일정책 설정")
    @PatchMapping("{id}/policy/date")
    public ResponseEntity<String> setUserDatePolicy(@RequestBody UserPolicyDto dto, @PathVariable Long id) throws CantFindByIdException {
        userService.setUserDatePolicy(dto, id);

        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="일반 유저 메뉴정책 설정", description="유저 메뉴정책 설정")
    @PatchMapping("{user_id}/policy/menu/{menu_id}")
    public ResponseEntity<String> setUserMenuPolicy(@PathVariable Long user_id, @PathVariable Long menu_id) throws CantFindByIdException, WrongParameter {
        userService.setUserMenuPolicy(user_id, menu_id);

        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="일반 유저 정책 조회", description="유저 요일정책 조회")
    @GetMapping("{id}/policy/date")
    public ResponseEntity<UserPolicyDto> getUserPolicy(@PathVariable Long id) throws CantFindByIdException {
        UserPolicyDto dto = userService.getUserPolicy(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}