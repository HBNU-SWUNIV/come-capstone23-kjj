package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class StoreUserApiController {

    private final StoreService storeService;

    @Operation(summary="식당 정보 조회")
    @GetMapping("store")
    public ResponseEntity<StoreDto> getStoreData() throws CantFindByIdException {
        return ResponseEntity.ok(storeService.getStoreData());
    }
}
