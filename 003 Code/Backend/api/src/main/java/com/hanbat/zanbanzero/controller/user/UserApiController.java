package com.hanbat.zanbanzero.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.*;
import com.hanbat.zanbanzero.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @Operation(summary="회원가입", description="username과 password를 입력받아 회원가입 시도")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserDto dto) throws JsonProcessingException, WrongRequestDetails {
        userService.join(dto);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    @Operation(summary="로그인", description="username과 password를 입력받아 로그인 시도")
    @PostMapping("/login/user")
    public ResponseEntity<String> login() {
        return ResponseEntity.status(HttpStatus.OK).body("로그인에 성공했습니다.");
    }

    @Operation(summary="회원탈퇴", description="username과 password를 입력받아 회원탈퇴")
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody UserDto dto) throws CantFindByIdException, WrongRequestDetails {
        userService.withdraw(dto);

        return ResponseEntity.status(HttpStatus.OK).body("탈퇴되었습니다.");
    }

    @Operation(summary="아이디 중복 체크", description="username만 입력받아 중복체크")
    @PostMapping("/join/check")
    public ResponseEntity<String> check(@RequestBody UserDto dto) {
        boolean result = userService.check(dto);
        if (result) return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 아이디입니다.");
        else return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @Operation(summary="일반회원 대표정보 조회", description="username만 입력받아 토큰과 비교하여 정보 제공")
    @GetMapping("/api/user/info")
    public ResponseEntity<UserInfoDto> getInfo(@RequestBody UserDto dto) throws JwtException {
        UserInfoDto user = userService.getInfo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary="일반 유저 마이페이지 조회", description="유저 상세정보 조회")
    @GetMapping("/api/user/{id}/page")
    public ResponseEntity<UserMypageDto> getMyPage(@PathVariable Long id) throws CantFindByIdException, JsonProcessingException {
        UserMypageDto userMyPageDto = userService.getMyPage(id);

        return ResponseEntity.status(HttpStatus.OK).body(userMyPageDto);
    }

    @Operation(summary="일반 유저 요일정책 설정", description="유저 요일정책 설정")
    @PatchMapping("/api/user/{id}/set/policy/date")
    public ResponseEntity<String> setUserDatePolicy(@RequestBody UserPolicyDto dto, @PathVariable Long id) throws CantFindByIdException {
        userService.setUserDatePolicy(dto, id);

        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="일반 유저 메뉴정책 설정", description="유저 메뉴정책 설정")
    @PatchMapping("/api/user/{user_id}/set/policy/menu/{menu_id}")
    public ResponseEntity<String> setUserMenuPolicy(@PathVariable Long user_id, @PathVariable Long menu_id) throws CantFindByIdException, WrongParameter {
        userService.setUserMenuPolicy(user_id, menu_id);

        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="일반 유저 정책 조회", description="유저 요일정책 조회")
    @GetMapping("/api/user/{id}/get/policy/date")
    public ResponseEntity<UserPolicyDto> getUserPolicy(@PathVariable Long id) throws CantFindByIdException {
        UserPolicyDto dto = userService.getUserPolicy(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}