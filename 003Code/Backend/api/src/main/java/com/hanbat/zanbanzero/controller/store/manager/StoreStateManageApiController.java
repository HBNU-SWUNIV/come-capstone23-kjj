package com.hanbat.zanbanzero.controller.store.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.calculate.CalculatePreWeekDto;
import com.hanbat.zanbanzero.dto.sbiz.WeeklyFoodPredictDto;
import com.hanbat.zanbanzero.dto.store.StorePreDto;
import com.hanbat.zanbanzero.dto.store.StoreSalesDto;
import com.hanbat.zanbanzero.dto.store.StoreTodayDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "정산 - 관리자 컨트롤러", description = "관리자 전용 정산 관련 API")
@RequiredArgsConstructor
@RestControllerClass("/api/manager/state")
public class StoreStateManageApiController {

    private final StoreService storeService;

    @Operation(summary="금일 / 전 영업일 매출액 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("store/sales")
    public ResponseEntity<StoreSalesDto> getSales() {
        return ResponseEntity.ok(storeService.getSales());
    }

    /**
     * 금일 이용자 수 조회
     * 10:30 마다 정산되어 갱신됨
     *
     * @return Integer
     */
    @Operation(summary="금일 / 전 영업일 이용자 수 조회", description="10:30분마다 정산하여 갱신됨")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/today")
    public ResponseEntity<StoreTodayDto> getToday() {
        return ResponseEntity.ok(storeService.getToday());
    }

    /**
     * 지난 주 이용자 수 조회
     * 월 ~ 금 5개 데이터
     *
     * @return List<StoreWeekendDto>
     */
    @Operation(summary="지난주 이용자 수 조회", description="월~금 5개 데이터")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/last-week/user")
    public ResponseEntity<List<StoreWeekendDto>> getLastWeeksUser() {
        return ResponseEntity.ok(storeService.getLastWeeksUser());
    }

    @Operation(summary="다음 주 이용자 수 조회", description="월~금 5개 데이터, 매일 오전 9시마다 계산")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/next-week/user")
    public ResponseEntity<CalculatePreWeekDto> getNextWeeksUser() {
        return ResponseEntity.ok(storeService.getNextWeeksUser());
    }

    @Operation(summary="다음 주 매출액 기반, 예약 기반 식재료 필요량 조회", description="월~금 5개 데이터, 매일 9시마다 계산")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/next-week/food")
    public ResponseEntity<WeeklyFoodPredictDto> getNextWeeksFood() {
        return ResponseEntity.ok(storeService.getNextWeeksFood());
    }

    /**
     * 총 누적 이용자 수 조회
     *
     * @return Integer
     */
    @Operation(summary="총 누적 이용자 수 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/all")
    public ResponseEntity<Integer> getAllUsers() {
        return ResponseEntity.ok(storeService.getAllUsers());
    }

    /**
     * 익일 예측 이용자 수 조회
     * 10시 30분마다 갱신
     *
     * @return Integer
     */
    @Operation(summary="익일 예측 / 금일 이용자 수", description = "10시 30분마다 갱신")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/predict/user")
    public ResponseEntity<StorePreDto> getCalculatePreUser() {
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "데이터 변환과정에서 문제 발생"),
            @ApiResponse(responseCode = "400", description = "정산 도중 문제가 발생하여 데이터가 생성되지 않은 경우")
    })
    @GetMapping("/predict/food")
    public ResponseEntity<Map<String, Integer>> getCalculatePreFood() throws JsonProcessingException, CantFindByIdException {
        return ResponseEntity.ok(storeService.getCalculatePreFood());
    }

    /**
     * 익일 예측 메뉴별 판매량 조회
     * 10시 30분마다 갱신
     * @return Map<String, Integer>
     * @throws JsonProcessingException - Map 파싱 에러
     */
    @Operation(summary="익일 예측 메뉴별 판매량 조회", description = "10시 30분마다 갱신")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "데이터 변환과정에서 문제 발생"),
            @ApiResponse(responseCode = "400", description = "정산 도중 문제가 발생하여 데이터가 생성되지 않은 경우")
    })
    @GetMapping("/predict/menu")
    public ResponseEntity<Map<String, Integer>> getCalculatePreMenu() throws JsonProcessingException, CantFindByIdException {
        return ResponseEntity.ok(storeService.getCalculatePreMenu());
    }
}
