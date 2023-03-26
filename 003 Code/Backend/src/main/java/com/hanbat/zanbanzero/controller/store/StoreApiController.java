package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;

    @PostMapping("/api/manager/store/set/location")
    public ResponseEntity<String> setLocation(@RequestBody StoreDto storeDto) throws CantFindByIdException, WrongRequestDetails {
        storeService.setLocation(storeDto.getLat(), storeDto.getLon());
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @GetMapping("/api/manager/store/location")
    public ResponseEntity<Map<String, Long>> getLocation() throws CantFindByIdException {
        Map<String, Long> result = storeService.getLocation();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/api/manager/store/set/congestion")
    public ResponseEntity<String> setCongestion(@RequestBody StoreStateDto storeStateDto) throws CantFindByIdException {
        storeService.setCongestion(storeStateDto);
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @GetMapping("/api/manager/store/congestion")
    public ResponseEntity<Long> getCongestion() throws CantFindByIdException {
        Long result = storeService.getCongestion();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
