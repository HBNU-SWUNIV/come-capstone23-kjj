package com.hanbat.zanbanzero.controller.store.manager;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.store.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "식당 - 관리자 컨트롤러", description = "관리자 전용 식당 관련 API")
@RequiredArgsConstructor
@RestControllerClass("/api/manager/store")
public class StoreManagerApiController {

    private final StoreService storeService;

    /**
     * 식당 정보 데이터 유무 확인
     * 없으면 null
     *
     * @return StoreDto
     */
    @Operation(summary="식당 정보 세팅 확인", description="관리자 로그인시 세팅 여부 확인 / 없으면 null")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식당 정보 조회 성공")
    })
    @GetMapping("/setting")
    public ResponseEntity<StoreDto> isSetting() {
        return ResponseEntity.ok(storeService.isSetting());
    }

    /**
     * 식당 정보 데이터 추가
     *
     * @param dto - name, info
     * @return StoreDto
     * @throws SameNameException - 식당 정보 데이터가 이미 존재할 경우 발생
     */
    @Operation(summary="식당 정보 세팅", description="세팅정보 없을 시 세팅")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설정 성공"),
            @ApiResponse(responseCode = "400", description = "이미 데이터가 존재하는 경우")
    })
    @PostMapping("/setting")
    public ResponseEntity<StoreDto> setSetting(@RequestBody StoreSettingDto dto) throws SameNameException {
        return ResponseEntity.ok(storeService.setSetting(dto));
    }

    /**
     * 식당 이미지 등록, 수정
     * form-data 필요
     *
     * @param file - 이미지 파일
     * @throws CantFindByIdException - 식당 정보 데이터 없을 때 발생
     */
    @Operation(summary = "식당 이미지 등록, 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설정 성공"),
            @ApiResponse(responseCode = "400", description = "식당 데이터가 존재하지 않는 경우")
    })
    @PostMapping("/image")
    public ResponseEntity<String> setStoreImage(@RequestPart(value = "file") MultipartFile file) throws CantFindByIdException, IOException {
        storeService.setStoreImage(file);
        return ResponseEntity.ok("반영되었습니다.");
    }

    /**
     * 단체명 수정
     *
     * @param dto - title
     * @return StoreDto
     * @throws CantFindByIdException - 식당 데이터 없을 때 발생
     */
    @Operation(summary="단체명 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "식당 데이터가 존재하지 않는 경우")
    })
    @PatchMapping("/title")
    public ResponseEntity<StoreDto> updateStoreTitle(@RequestBody StoreTitleDto dto) throws CantFindByIdException{
        return ResponseEntity.ok(storeService.updateStoreTitle(dto));
    }

    /**
     * 식당 소개 수정
     *
     * @param dto - info
     * @return StoreDto
     * @throws CantFindByIdException - 식당 데이터 없을 때 발생
     */
    @Operation(summary="식당 소개 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "식당 데이터가 존재하지 않는 경우")
    })
    @PatchMapping("/info")
    public ResponseEntity<StoreDto> updateStoreInfo(@RequestBody StoreInfoDto dto) throws CantFindByIdException {
        return ResponseEntity.ok(storeService.updateStoreInfo(dto));
    }

    /**
     * 연, 월, 일, 휴무일 설정
     * @param off - boolean off
     * @param year - 연
     * @param month - 월
     * @param day - 일
     * @return StoreStateDto
     */
    @Operation(summary="휴무일 설정", description="n월 n일의 휴무일 설정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설정 성공")
    })
    @PostMapping("/off/{year}/{month}/{day}")
    public ResponseEntity<StoreStateDto> setOff(@RequestBody StoreOffDto off, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(storeService.setOff(off, year, month, day));
    }
}
