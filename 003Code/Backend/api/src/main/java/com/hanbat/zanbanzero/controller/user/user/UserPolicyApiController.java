package com.hanbat.zanbanzero.controller.user.user;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.util.JwtUtil;
import com.hanbat.zanbanzero.dto.user.user.UserDatePolicyDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.user.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "유저 정책 컨트롤러", description = "유저 정책 관련 API")
@RequiredArgsConstructor
@RestControllerClass("/api/user/policy")
public class UserPolicyApiController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate = new JwtTemplate();

    /**
     * 일반 유저 요일 정책 설정
     *
     * @param dto - monday ~ friday bool값
     * @return UserPolicyDto
     * @throws CantFindByIdException - userPolicy가 없을 경우 발생
     */
    @Operation(summary="일반 유저 요일정책 설정", description="유저 요일정책 설정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설정 성공"),
            @ApiResponse(responseCode = "400", description = "token에 작성된 id가 잘못된 경우")
    })
    @PatchMapping("/date")
    public ResponseEntity<UserPolicyDto> setUserDatePolicy(HttpServletRequest request, @RequestBody UserDatePolicyDto dto) throws CantFindByIdException {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.setUserDatePolicy(dto, id));
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설정 성공"),
            @ApiResponse(responseCode = "400", description = "token에 작성된 id가 잘못된 경우"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우"),
    })
    @PatchMapping("/menu/{menuId}")
    public ResponseEntity<UserPolicyDto> setUserMenuPolicy(HttpServletRequest request, @PathVariable Long menuId) throws CantFindByIdException, WrongParameter {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.setUserMenuPolicy(id, menuId));
    }

    @Operation(summary="일반 유저 정책 조회", description="유저 요일정책 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "token에 작성된 id가 잘못된 경우")
    })
    @GetMapping("/date")
    public ResponseEntity<UserPolicyDto> getUserPolicy(HttpServletRequest request) throws CantFindByIdException {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getUserPolicy(id));
    }
}