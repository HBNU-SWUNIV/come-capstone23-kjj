package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.image.ImageService;
import com.hanbat.zanbanzero.service.menu.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "메뉴 - 관리자 컨트롤러", description = "관리자 전용 메뉴 API")
@RequiredArgsConstructor
@RestControllerClass("/api/manager/menu")
public class MenuManagerApiController {

    private final MenuService menuService;
    private final ImageService menuImageService;

    private final String uploadDir = "img/menu";

    /**
     * Manager용 전체 메뉴 조회
     *
     * @return List<MenuManagerInfoDto>
     */
    @Operation(summary="전체 메뉴 조회 - 관리자 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자용 전체 메뉴 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<List<MenuManagerInfoDto>> getMenusForManager() {
        return ResponseEntity.ok(menuService.getMenusForManager());
    }

    /**
     * 식단표 사용중인 메뉴 유무
     * 
     * @return bool
     */
    @Operation(summary="식단표 사용 메뉴 유무 조회", description="true / false")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공"),
    })
    @GetMapping("/planner")
    public ResponseEntity<Boolean> isPlanned() {
        return ResponseEntity.ok(menuService.isPlanned());
    }

    /**
     * 식단표 사용 설정
     * 
     * @param menuId - menu ID
     * @return Boolean
     * @throws CantFindByIdException - menu가 없을 때 발생
     * @throws WrongParameter - 이미 식단표를 사용중일 때 발생
     */
    @Operation(summary="식단표 사용 설정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미 식단표를 사용하는 메뉴가 존재하는 경우 false"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우")
    })
    @PostMapping("/{menuId}/planner")
    public ResponseEntity<Boolean> setPlanner(@PathVariable Long menuId) throws CantFindByIdException {
        return ResponseEntity.ok(menuService.setPlanner(menuId));
    }

    /**
     * 식단표 교체
     * @param menuId - menu ID
     * @return Boolean
     * @throws CantFindByIdException - 메뉴가 없을 때 발생
     */
    @Operation(summary="식단표 교체 설정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경에 성공한 경우"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우")
    })
    @PatchMapping("/{menuId}/change/planner")
    public ResponseEntity<Boolean> changePlanner(@PathVariable Long menuId) throws CantFindByIdException {
        return ResponseEntity.ok(menuService.changePlanner(menuId));
    }

    /**
     * 메뉴 추가
     * form-data 형식 필요
     * @param dto - name, cost, info, details, usePlanner
     * @param file - 이미지 파일
     * @return MenuDto
     * @throws SameNameException - 동일 이름 메뉴가 존재할 때 발생
     * @throws WrongParameter - 하나라도 null이 존재할 때 발생
     * @throws UploadFileException - 이미지 업로드 도중 에러 발생
     */
    @Operation(summary="관리자 - 메뉴 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메뉴 추가 성공"),
            @ApiResponse(responseCode = "400", description = "dto가 null이거나 비어있는 필드가 존재하는 경우")
    })
    @PostMapping
    public ResponseEntity<MenuDto> addMenu(@RequestPart("data") MenuUpdateDto dto, @RequestPart(value = "file", required = false)MultipartFile file) throws SameNameException, WrongParameter, UploadFileException {
        if (dto == null) throw new WrongParameter("dto : null");
        else if (!dto.check()) throw new WrongParameter("dto : " + dto);

        String filePath = (file != null) ? menuImageService.uploadImage(file, uploadDir) : null;
        return ResponseEntity.ok(menuService.addMenu(dto, filePath));
    }

    /**
     * 식자재 정보 추가
     * 
     * @param menuId - 메뉴 아이디
     * @param foodId - 식재료 아이디
     * @return MenuFoodDto
     * @throws CantFindByIdException - 메뉴가 없을 때 발생
     */
    @Operation(summary="관리자 - 메뉴에 식재료 정보 등록", description = "foodId가 0이면 식재료 정보 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식재료 등록 성공"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우")
    })
    @PatchMapping("/{menuId}/food/{foodId}")
    public ResponseEntity<Boolean> setFood(@PathVariable Long menuId, @PathVariable Long foodId) throws CantFindByIdException {
        return ResponseEntity.ok(menuService.setFood(menuId, foodId));
    }

    /**
     * 식자재 정보 조회
     *
     * @return Map<String, Integer>
     */
    @Operation(summary="관리자 - 전체 식재료 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 식재료 정보 조회 성공"),
    })
    @GetMapping("/food")
    public ResponseEntity<List<MenuFoodDto>> getFood() {
        return ResponseEntity.ok(menuService.getFood().dtos());
    }

    @Operation(summary="관리자 - 특정 식재료 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 식재료 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "foodId가 잘못된 경우")
    })
    @GetMapping("/food/{foodId}")
    public ResponseEntity<MenuFoodDto> getOneFood(@PathVariable Long foodId) throws CantFindByIdException {
        return ResponseEntity.ok(menuService.getOneFood(foodId));
    }

    /**
     * 메뉴 수정
     * form-data 필요
     * 
     * @param dto - name, cost, info, details, usePlanner
     * @param file - 이미지 파일
     * @param menuId - menu ID
     * @return MenuInfoDto
     * @throws CantFindByIdException - 메뉴가 없을 때 발생
     * @throws IOException - 이미지 업로드 중 에러 발생
     */
    @Operation(summary="관리자 - 메뉴 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메뉴 수정 성공"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우")
    })
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuInfoDto> updateMenu(@RequestPart("data") MenuUpdateDto dto, @RequestPart(value = "file", required = false)MultipartFile file, @PathVariable Long menuId) throws CantFindByIdException, IOException{
        return ResponseEntity.ok(menuService.updateMenu(dto, file, menuId, uploadDir));
    }

    /**
     * 메뉴 삭제
     * 
     * @param menuId - menu ID
     * @return MenuDto
     * @throws CantFindByIdException - 메뉴가 없을 경우 발생
     */
    @Operation(summary="관리자 - 메뉴 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메뉴 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우")
    })
    @DeleteMapping("/{menuId}")
    public ResponseEntity<MenuDto> deleteMenu(@PathVariable Long menuId) throws CantFindByIdException {
        return ResponseEntity.ok(menuService.deleteMenu(menuId));
    }

    /**
     * 품절 지정
     * 
     * @param menuId - menu ID
     * @param type - y / n
     * @return MenuDto
     * @throws CantFindByIdException - 메뉴가 없을 때 발생
     * @throws WrongParameter - type이 y, n이 아닐 때 발생
     */
    @Operation(summary="관리자 - 품절 지정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "품절 지정 성공"),
            @ApiResponse(responseCode = "400", description = "menuId가 잘못된 경우"),
            @ApiResponse(responseCode = "400", description = "type이 잘못된 경우(y, n)")
    })
    @PatchMapping("/{menuId}/sold/{type}")
    public ResponseEntity<Boolean> setSoldOut(@PathVariable Long menuId, @PathVariable String type) throws CantFindByIdException, WrongParameter {
        return ResponseEntity.ok(menuService.setSoldOut(menuId, type));
    }
}