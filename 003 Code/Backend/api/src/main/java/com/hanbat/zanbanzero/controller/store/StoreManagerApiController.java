package com.hanbat.zanbanzero.controller.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreOffDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary="식당 정보 세팅 확인", description="관리자 로그인시 세팅 여부 확인 / 없으면 null")
    @GetMapping("setting")
    public ResponseEntity<StoreDto> isSetting() {
        return ResponseEntity.ok(storeService.isSetting());
    }

    @Operation(summary="식당 정보 세팅", description="세팅정보 없을 시 세팅")
    @PostMapping("setting")
    public ResponseEntity<StoreDto> setSetting(@RequestBody StoreDto dto) throws SameNameException {
        return ResponseEntity.ok(storeService.setSetting(dto));
    }

    @Operation(summary = "식당 이미지 등록, 수정")
    @PostMapping("image")
    public ResponseEntity<String> setStoreImage(@RequestPart(value = "file") MultipartFile file) throws CantFindByIdException, IOException {
        storeService.setStoreImage(file);
        return ResponseEntity.ok("반영되었습니다.");
    }

    @Operation(summary="금일 이용자 수 조회", description="10:30분마다 정산하여 갱신됨")
    @GetMapping("state/today")
    public ResponseEntity<Integer> getToday() {
        return ResponseEntity.ok(storeService.getToday());
    }

    @Operation(summary="지난주 이용자 수 조회", description="월~금 5개 데이터")
    @GetMapping("state/last-week/user")
    public ResponseEntity<List<StoreWeekendDto>> getLastWeeksUser() throws WrongParameter {
        return ResponseEntity.ok(storeService.getLastWeeksUser());
    }

    @Operation(summary="총 누적 이용자 수 조회")
    @GetMapping("state/all")
    public ResponseEntity<Integer> getAllUsers() {
        return ResponseEntity.ok(storeService.getAllUsers());
    }

    @Operation(summary = "최근 5영업일 메뉴별 판매량 조회", description = "메뉴 3종류만")
    @GetMapping("state/menu")
    public ResponseEntity<List<CalculateMenuForGraphDto>> getPopularMenus() {
        return ResponseEntity.ok(storeService.getPopularMenus());
    }

    @Operation(summary="단체명 수정")
    @PatchMapping("store/title")
    public ResponseEntity<StoreDto> updateStoreTitle(@RequestBody StoreDto dto) throws CantFindByIdException{
        return ResponseEntity.ok(storeService.updateStoreTitle(dto));
    }

    @Operation(summary="식당 소개 수정")
    @PatchMapping("store/info")
    public ResponseEntity<StoreDto> updateStoreInfo(@RequestBody StoreDto dto) throws CantFindByIdException {
        return ResponseEntity.ok(storeService.updateStoreInfo(dto));
    }

    @Operation(summary="휴무일 설정", description="n월 n일의 휴무일 설정")
    @PostMapping("store/off/{year}/{month}/{day}")
    public ResponseEntity<StoreStateDto> setOff(@RequestBody StoreOffDto off, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(storeService.setOff(off.isOff(), year, month, day));
    }

    @Operation(summary="월간 휴무일 조회", description="n월 한달의 휴무일 조회")
    @GetMapping("store/off/{year}/{month}")
    public ResponseEntity<List<StoreStateDto>> getClosedDays(@PathVariable int year, @PathVariable int month) throws WrongParameter {
        if (0 >= month || month > 12) throw new WrongParameter("잘못된 입력입니다.");
        return ResponseEntity.ok(storeService.getClosedDays(year, month));
    }

    @Operation(summary="익일 예측 이용자 수", description = "10시 30분 10초마다 갱신")
    @GetMapping("state/predict/user")
    public ResponseEntity<Integer> getCalculatePreUser() {
        return ResponseEntity.ok(storeService.getCalculatePreUser());
    }

    @Operation(summary="익일 예측 식쟤료 소비량 데이터 조회", description = "10시 30분 10초마다 갱신")
    @GetMapping("state/predict/food")
    public ResponseEntity<Map<String, Integer>> getCalculatePreFood() throws JsonProcessingException {
        return ResponseEntity.ok(storeService.getCalculatePreFood());
    }

    @Operation(summary="익일 예측 메뉴별 판매량 조회", description = "10시 30분 10초마다 갱신")
    @GetMapping("state/predict/menu")
    public ResponseEntity<Map<String, Integer>> getCalculatePreMenu() throws JsonProcessingException {
        return ResponseEntity.ok(storeService.getCalculatePreMenu());
    }
}
