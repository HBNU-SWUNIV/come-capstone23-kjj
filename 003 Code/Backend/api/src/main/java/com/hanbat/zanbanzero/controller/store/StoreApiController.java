package com.hanbat.zanbanzero.controller.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.calculate.CalculateDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;

    @Operation(summary="식당 정보 세팅 확인", description="관리자 로그인시 세팅 여부 확인 / 없으면 null")
    @GetMapping("/api/manager/isSetting")
    public ResponseEntity<StoreDto> isSetting() {
        StoreDto result = storeService.isSetting();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식당 정보 세팅", description="세팅정보 없을 시 세팅")
    @PostMapping("/api/manager/setSetting")
    public ResponseEntity<String> setSetting(@RequestBody StoreDto dto) throws SameNameException {
        storeService.setSetting(dto);
        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="금일 이용자 수 조회", description="10:30분마다 정산하여 갱신됨")
    @GetMapping("/api/manager/get/state/today")
    public ResponseEntity<Integer> getToday() throws JsonProcessingException {
        Integer result = storeService.getToday();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="최근 이용자 수 조회", description="최근 5개 데이터")
    @GetMapping("/api/manager/get/state/weekend")
    public ResponseEntity<List<StoreWeekendDto>> getWeekend() {
        List<StoreWeekendDto> result = storeService.getWeekend();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="총 누적 이용자 수 조회", description="")
    @GetMapping("/api/manager/get/state/all")
    public ResponseEntity<Integer> getAllUsers() {
        int result = storeService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식당 정보 조회", description="")
    @GetMapping("/api/user/store")
    public ResponseEntity<StoreDto> getStoreData() throws CantFindByIdException, WrongRequestDetails {
        StoreDto result = storeService.getStoreData();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식당 소개 수정", description="")
    @PatchMapping("/api/manager/store/set/info")
    public ResponseEntity<StoreDto> updateStoreInfo(@RequestBody StoreDto dto) throws CantFindByIdException, WrongRequestDetails {
        StoreDto result = storeService.updateStoreInfo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="휴무일 설정", description="n월 n일의 휴무일 설정")
    @PostMapping("/api/manager/store/set/off/{year}/{month}/{day}")
    public ResponseEntity<String> setOff(@RequestBody Boolean off, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        storeService.setOff(off, year, month, day);
        return ResponseEntity.ok().body("저장되었습니다.");
    }

    @Operation(summary="월간 휴무일 조회", description="n월 한달의 휴무일 조회")
    @GetMapping("/api/manager/store/get/off/{year}/{month}")
    public ResponseEntity<List<StoreStateDto>> getPlanner(@PathVariable int year, @PathVariable int month) throws WrongParameter {
        List<StoreStateDto> result = storeService.getOffOfMonth(year, month);
        return ResponseEntity.ok().body(result);
    }
}
