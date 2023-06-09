package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuManagerInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.menu.MenuImageService;
import com.hanbat.zanbanzero.service.menu.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class MenuUserApiController {

    private final MenuService menuService;

    @Operation(summary="전체 메뉴 조회")
    @GetMapping("menu")
    public ResponseEntity<List<MenuDto>> getMenus() {
        List<MenuDto> menus = menuService.getMenus();
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }

    @Operation(summary="특정 메뉴 상세정보 조회")
    @GetMapping("menu/{id}")
    public ResponseEntity<MenuInfoDto> getMenuInfo(@PathVariable Long id) throws CantFindByIdException {
        MenuInfoDto menuDto = menuService.getMenuInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(menuDto);
    }
}