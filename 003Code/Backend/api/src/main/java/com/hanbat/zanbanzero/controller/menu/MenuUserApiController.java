package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDto;
import com.hanbat.zanbanzero.service.menu.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "메뉴 - 관리자 컨트롤러", description = "관리자 전용 메뉴 API")
@RequiredArgsConstructor
@RestControllerClass("/api/user/menu")
public class MenuUserApiController {

    private final MenuService menuService;

    /**
     * User용 전체 메뉴 조회 api
     *
     * @return List<MenuUserInfoDto>
     */
    @Operation(summary="전체 메뉴 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 메뉴 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<List<MenuUserInfoDto>> getMenus() {
        return ResponseEntity.ok(menuService.getMenus().dtos());
    }
}