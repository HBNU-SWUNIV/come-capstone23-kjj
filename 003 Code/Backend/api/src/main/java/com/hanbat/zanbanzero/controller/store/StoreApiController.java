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

    @Operation(summary="식당 정보 세팅 확인", description="관리자 로그인시 세팅 여부 확인")
    @GetMapping("/api/manager/isSetting")
    public ResponseEntity<Boolean> isSetting() {
        boolean result = storeService.isSetting();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식당 정보 세팅", description="세팅정보 없을 시 세팅")
    @PostMapping("/api/manager/setSetting")
    public ResponseEntity<String> setSetting(@RequestBody StoreDto dto) {
        storeService.setSetting(dto);
        storeService.setStoreState();
        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }


    @Operation(summary="식당 정보 조회", description="")
    @GetMapping("/api/user/store")
    public ResponseEntity<StoreDto> getStoreData() throws CantFindByIdException, WrongRequestDetails {
        StoreDto result = storeService.getStoreData();
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
