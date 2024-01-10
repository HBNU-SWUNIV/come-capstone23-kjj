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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "마이 페이지 컨트롤러", description = "마이 페이지 관련 API")
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "token에 작성된 id가 잘못된 경우")
    })
    @GetMapping
    public ResponseEntity<UserMypageDto> getMyPage(HttpServletRequest request) throws CantFindByIdException {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.getMyPage(id));
    }

    @Operation(summary="포인트 사용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 성공"),
            @ApiResponse(responseCode = "400", description = "token에 작성된 id가 잘못된 경우"),
            @ApiResponse(responseCode = "400", description = "포인트가 부족한 경우")
    })
    @PostMapping("/point")
    public ResponseEntity<Integer> usePoint(HttpServletRequest request, @RequestBody UsePointDto dto) throws CantFindByIdException, WrongRequestDetails {
        Long id = jwtUtil.getIdFromToken(request.getHeader(jwtTemplate.getHeaderString()));

        return ResponseEntity.ok(userService.usePoint(id, dto));
    }
}