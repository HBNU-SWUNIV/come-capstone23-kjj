package com.hanbat.zanbanzero.controller.user.user;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.util.JwtUtil;
import com.hanbat.zanbanzero.dto.user.user.UsePointDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.user.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestControllerClass("/api/user/page")
public class UserPageController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate = new JwtTemplate();

    /**
     * 일반 유저 마이페이지 조회
     * userId, point
     *
     * @return UserMypageDto
     * @throws CantFindByIdException - userMyPage가 없을 경우 발생
     */
    @Operation(summary="일반 유저 마이페이지 조회", description="유저 상세정보 조회")
    @GetMapping
    public ResponseEntity<UserMypageDto> getMyPage(HttpServletRequest request) throws CantFindByIdException {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getMyPage(id));
    }

    @Operation(summary="포인트 사용")
    @PostMapping("/point")
    public ResponseEntity<Integer> usePoint(HttpServletRequest request, @RequestBody UsePointDto dto) throws CantFindByIdException, WrongRequestDetails {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.usePoint(id, dto));
    }
}