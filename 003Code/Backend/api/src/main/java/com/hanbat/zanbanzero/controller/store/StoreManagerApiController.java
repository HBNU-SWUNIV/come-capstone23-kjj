package com.hanbat.zanbanzero.controller.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.calculate.CalculatePreWeekDto;
import com.hanbat.zanbanzero.dto.store.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/")
public class StoreManagerApiController {

    private final StoreService storeService;

    /**
     * 식당 정보 데이터 유무 확인
     * 없으면 null
     *
     * @return StoreDto
     */
    @Operation(summary="식당 정보 세팅 확인", description="관리자 로그인시 세팅 여부 확인 / 없으면 null")
    @GetMapping("setting")
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
    @PostMapping("setting")
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
    @PostMapping("image")
    public ResponseEntity<String> setStoreImage(@RequestPart(value = "file") MultipartFile file) throws CantFindByIdException, IOException {
        storeService.setStoreImage(file);
        return ResponseEntity.ok("반영되었습니다.");
    }

    /**
     * 금일 이용자 수 조회
     * 10:30 마다 정산되어 갱신됨
     *
     * @return Integer
     */
    @Operation(summary="금일 이용자 수 조회", description="10:30분마다 정산하여 갱신됨")
    @GetMapping("state/today")
    public ResponseEntity<Integer> getToday() {
        return ResponseEntity.ok(storeService.getToday());
    }

    /**
     * 지난 주 이용자 수 조회
     * 월 ~ 금 5개 데이터
     *
     * @return List<StoreWeekendDto>
     */
    @Operation(summary="지난주 이용자 수 조회", description="월~금 5개 데이터")
    @GetMapping("state/last-week/user")
    public ResponseEntity<List<StoreWeekendDto>> getLastWeeksUser() {
        return ResponseEntity.ok(storeService.getLastWeeksUser());
    }

    @Operation(summary="다음 주 이용자 수 조회", description="월~금 5개 데이터, 일요일 10시 30분 정산")
    @GetMapping("state/next-week/user")
    public ResponseEntity<CalculatePreWeekDto> getNextWeeksUser() {
        return ResponseEntity.ok(storeService.getNextWeeksUser());
    }

    /**
     * 총 누적 이용자 수 조회
     *
     * @return Integer
     */
    @Operation(summary="총 누적 이용자 수 조회")
    @GetMapping("state/all")
    public ResponseEntity<Integer> getAllUsers() {
        return ResponseEntity.ok(storeService.getAllUsers());
    }

    /**
     * 단체명 수정
     *
     * @param dto - title
     * @return StoreDto
     * @throws CantFindByIdException - 식당 데이터 없을 때 발생
     */
    @Operation(summary="단체명 수정")
    @PatchMapping("store/title")
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
    @PatchMapping("store/info")
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
    @PostMapping("store/off/{year}/{month}/{day}")
    public ResponseEntity<StoreStateDto> setOff(@RequestBody StoreOffDto off, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(storeService.setOff(off, year, month, day));
    }

    /**
     * 익일 예측 이용자 수 조회
     * 10시 30분마다 갱신
     *
     * @return Integer
     */
    @Operation(summary="익일 예측 이용자 수", description = "10시 30분마다 갱신")
    @GetMapping("state/predict/user")
    public ResponseEntity<Integer> getCalculatePreUser() {
        return ResponseEntity.ok(storeService.getCalculatePreUser());
    }

    /**
     * 익일 예측 식재료 소비량 조회
     * 10시 30분마다 갱신
     *
     * @return Map<String, Integer>
     * @throws JsonProcessingException - Map 파싱 에러
     */
    @Operation(summary="익일 예측 식재료 소비량 데이터 조회", description = "10시 30분마다 갱신")
    @GetMapping("state/predict/food")
    public ResponseEntity<Map<String, Integer>> getCalculatePreFood() throws JsonProcessingException {
        return ResponseEntity.ok(storeService.getCalculatePreFood());
    }

    /**
     * 익일 예측 메뉴별 판매량 조회
     * 10시 30분마다 갱신
     * @return Map<String, Integer>
     * @throws JsonProcessingException - Map 파싱 에러
     */
    @Operation(summary="익일 예측 메뉴별 판매량 조회", description = "10시 30분마다 갱신")
    @GetMapping("state/predict/menu")
    public ResponseEntity<Map<String, Integer>> getCalculatePreMenu() throws JsonProcessingException {
        return ResponseEntity.ok(storeService.getCalculatePreMenu());
    }
}
