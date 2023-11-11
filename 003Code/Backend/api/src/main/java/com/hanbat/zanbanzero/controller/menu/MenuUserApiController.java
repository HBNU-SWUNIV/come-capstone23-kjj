package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDto;
import com.hanbat.zanbanzero.service.menu.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<MenuUserInfoDto>> getMenus() {
        return ResponseEntity.ok(menuService.getMenus().getDtos());
    }
}