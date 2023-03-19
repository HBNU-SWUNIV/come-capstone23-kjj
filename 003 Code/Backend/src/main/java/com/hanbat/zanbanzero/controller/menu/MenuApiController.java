package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    @GetMapping("/api/user/menu")
    public ResponseEntity<List<MenuDto>> getMenus() {
        List<MenuDto> menus = menuService.getMenus();
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }

    @GetMapping("/api/user/menu/{id}")
    public ResponseEntity<MenuInfoDto> getMenuInfo(@PathVariable Long id) throws CantFindByIdException {
        MenuInfoDto menuDto = menuService.getMenuInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(menuDto);
    }

    @PostMapping("/api/manager/menu/add")
    public ResponseEntity<String> addMenu(@RequestBody MenuUpdateDto dto) throws SameNameException, CantFindByIdException {
        menuService.addMenu(dto);
        return ResponseEntity.status(HttpStatus.OK).body("등록되었습니다.");
    }

    @PatchMapping("/api/manager/menu/{id}/update")
    public ResponseEntity<String> updateMenu(@RequestBody MenuUpdateDto dto, @PathVariable Long id) throws CantFindByIdException {
        menuService.updateMenu(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @PatchMapping("/api/manager/menu/{id}/info/update")
    public ResponseEntity<String> updateMenuInfo(@RequestBody MenuUpdateDto dto, @PathVariable Long id) throws CantFindByIdException {
        menuService.updateMenuInfo(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @DeleteMapping("/api/manager/menu/{id}/del")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) throws CantFindByIdException {
        menuService.deleteMenu(id);
        return ResponseEntity.status(HttpStatus.OK).body("삭제되었습니다.");
    }

    @PatchMapping("/api/manager/menu/{id}/sold/{type}")
    public ResponseEntity<String> setSoldOut(@PathVariable Long id, @PathVariable String type) throws CantFindByIdException, WrongParameter {
        menuService.setSoldOut(id, type);
        return ResponseEntity.status(HttpStatus.OK).body("반영되었습니다.");
    }
}