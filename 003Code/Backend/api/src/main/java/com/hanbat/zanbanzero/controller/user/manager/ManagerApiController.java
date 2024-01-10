package com.hanbat.zanbanzero.controller.user.manager;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.util.JwtUtil;
import com.hanbat.zanbanzero.dto.user.auth.LoginDto;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
import com.hanbat.zanbanzero.service.user.manager.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "관리자 컨트롤러", description = "관리자 관련 API")
@RequiredArgsConstructor
@RestControllerClass("/api/manager")
public class ManagerApiController {
    private final ManagerService managerService;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate = new JwtTemplate();

    /**
     * usernamer과 password로 관리자 로그인
     *
     * @return ManagerInfoDto
     */
    @Operation(summary="관리자 로그인", description="username과 password를 입력받아 로그인 시도")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "username이나 password가 잘못된 경우")
    })
    @RequestBody(content = @Content(schema = @Schema(implementation = LoginDto.class)))
    @PostMapping("/login/id")
    public ResponseEntity<ManagerInfoDto> managerLogin(HttpServletRequest request) throws CantFindByUsernameException {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(managerService.getInfoForUsername(username));
    }

    /**
     * 관리자 대표정보 조회
     * id, username
     *
     * @return ManagerInfoDto
     */
    @Operation(summary="관리자 대표정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "username이 잘못된 경우")
    })
    @GetMapping("/info")
    public ResponseEntity<ManagerInfoDto> getInfo(HttpServletRequest request) throws CantFindByUsernameException {
        String username = jwtUtil.getUsernameFromToken(request.getHeader(jwtTemplate.getHeaderString()));
        return ResponseEntity.ok(managerService.getInfo(username));
    }
}
