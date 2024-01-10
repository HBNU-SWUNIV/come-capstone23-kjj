package com.hanbat.zanbanzero.controller.store.user;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "식당 - 유저 컨트롤러", description = "유저 전용 식당 관련 API")
@RequiredArgsConstructor
@RestControllerClass("/api/user/store")
public class StoreUserApiController {

    private final StoreService storeService;

    /**
     * 식당 정보 조회
     *
     * @return StoreDto
     * @throws CantFindByIdException - 식당 정보가 없을 때 발생
     */
    @Operation(summary="식당 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "식당 정보가 없는 경우")
    })
    @GetMapping
    public ResponseEntity<StoreDto> getStoreData() throws CantFindByIdException {
        return ResponseEntity.ok(storeService.getStoreData());
    }

    /**
     * 연, 월 받아 휴무일 조회
     *
     * @param year - 연
     * @param month - 월
     * @return List<StoreStateDto>
     * @throws WrongParameter - month가 1 ~ 12가 아닐 때 발생
     */
    @Operation(summary="월간 휴무일 조회", description="n월 한달의 휴무일 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "month가 잘못된 경우")
    })
    @GetMapping("/off/{year}/{month}")
    public ResponseEntity<List<StoreStateDto>> getClosedDays(@PathVariable int year, @PathVariable int month) throws WrongParameter {
        if (0 >= month || month > 12) throw new WrongParameter("month(1 ~ 12) : " + month);
        return ResponseEntity.ok(storeService.getClosedDays(year, month));
    }
}
