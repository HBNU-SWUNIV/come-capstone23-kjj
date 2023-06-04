package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.dto.menu.MenuManagerInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
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
@RequestMapping("/api/manager/")
public class MenuManagerApiController {

    private final MenuService menuService;
    private final MenuImageService menuImageService;

    @Operation(summary="전체 메뉴 조회 - 관리자 전용")
    @GetMapping("menu")
    public ResponseEntity<List<MenuManagerInfoDto>> getMenusForManager() {
        List<MenuManagerInfoDto> menus = menuService.getMenusForManager();
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }

    @Operation(summary="식단표 사용 메뉴 유무 조회", description="true / false")
    @GetMapping("menu/planner")
    public ResponseEntity<Boolean> isPlanner() {
        Boolean result = menuService.isPlanner();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식단표 사용 설정")
    @PostMapping("menu/{id}/planner")
    public ResponseEntity<String> setPlanner(@PathVariable Long id) throws CantFindByIdException, WrongParameter {
        menuService.setPlanner(id);
        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="식단표 교체 설정")
    @PatchMapping("menu/{id}/change/planner")
    public ResponseEntity<String> changePlanner(@PathVariable Long id) throws CantFindByIdException {
        menuService.changePlanner(id);
        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="관리자 - 메뉴 추가")
    @PostMapping("menu/add")
    public ResponseEntity<String> addMenu(@RequestPart("data") MenuUpdateDto dto, @RequestPart(value = "file", required = false)MultipartFile file) throws SameNameException, CantFindByIdException {
        if (dto == null || !dto.check()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("데이터가 부족합니다.");

        String filePath = (file != null) ? menuImageService.uploadImage(file) : null;
        menuService.addMenu(dto, filePath);
        return ResponseEntity.status(HttpStatus.OK).body("등록되었습니다.");
    }

    @Operation(summary="관리자 - 메뉴 수정")
    @PatchMapping("menu/{id}/update")
    public ResponseEntity<String> updateMenu(@RequestPart("data") MenuUpdateDto dto, @RequestPart(value = "file", required = false)MultipartFile file, @PathVariable Long id) throws CantFindByIdException, IOException, SameNameException {
        menuService.updateMenu(dto, file, id);

        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @Operation(summary="관리자 - 메뉴 삭제")
    @DeleteMapping("menu/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) throws CantFindByIdException {
        menuService.deleteMenu(id);
        return ResponseEntity.status(HttpStatus.OK).body("삭제되었습니다.");
    }

    @Operation(summary="관리자 - 품절 지정")
    @PatchMapping("menu/{id}/sold/{type}")
    public ResponseEntity<String> setSoldOut(@PathVariable Long id, @PathVariable char type) throws CantFindByIdException, WrongParameter {
        menuService.setSoldOut(id, type);
        return ResponseEntity.status(HttpStatus.OK).body("반영되었습니다.");
    }
}