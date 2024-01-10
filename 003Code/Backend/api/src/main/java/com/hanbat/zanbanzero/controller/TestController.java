package com.hanbat.zanbanzero.controller;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.auth.util.JwtUtil;
import com.hanbat.zanbanzero.service.user.manager.ManagerService;
import com.hanbat.zanbanzero.service.user.user.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "테스트 컨트롤러")
@RequiredArgsConstructor
@RestControllerClass("/api")
public class TestController {
    private final UserService userService;
    private final ManagerService managerService;
    private final JwtUtil jwtUtil;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "테스트용 user 토큰 발급 성공"),
            @ApiResponse(responseCode = "403", description = "username이 잘못된 경우")
    })
    @GetMapping("/user/login/test")
    public ResponseEntity<Map<String, String>> testToken(@RequestParam("username") String username){
        if (username == null) username = "user";
        return ResponseEntity.ok(Map.of("accessToken", jwtUtil.createToken(userService.loadUserByUsername(username))));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "테스트용 manager 토큰 발급 성공"),
    })
    @GetMapping("/manager/login/test")
    public ResponseEntity<Map<String, String>> testTokenForManager(){
        String managerName = "manager";
        return ResponseEntity.ok(Map.of("accessToken", jwtUtil.createToken(managerService.loadUserByUsername(managerName))));
    }
}
