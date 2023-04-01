package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;

    @Operation(summary="가게 위치정보 설정", description="lat, lon 받아 지정")
    @PostMapping("/api/manager/store/set/location")
    public ResponseEntity<String> setLocation(@RequestBody StoreDto storeDto) throws CantFindByIdException, WrongRequestDetails {
        storeService.setLocation(storeDto.getLat(), storeDto.getLon());
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @Operation(summary="가게 위치정보 조회", description="lat, lon JSON 데이터 반환")
    @GetMapping("/api/manager/store/location")
    public ResponseEntity<Map<String, Long>> getLocation() throws CantFindByIdException {
        Map<String, Long> result = storeService.getLocation();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="가게 혼잡도 지정", description="0~100 숫자 입력")
    @PostMapping("/api/manager/store/set/congestion")
    public ResponseEntity<String> setCongestion(@RequestBody StoreStateDto storeStateDto) throws CantFindByIdException {
        storeService.setCongestion(storeStateDto);
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @Operation(summary="가게 혼잡도 확인", description="int값 반환")
    @GetMapping("/api/manager/store/congestion")
    public ResponseEntity<Long> getCongestion() throws CantFindByIdException {
        Long result = storeService.getCongestion();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
