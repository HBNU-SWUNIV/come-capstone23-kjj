package com.hanbat.zanbanzero.controller.store.user;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "정산 - 유저 컨트롤러", description = "유저 전용 정산 관련 API")
@RequiredArgsConstructor
@RestControllerClass("/api/user/state")
public class StoreStateUserApiController {

    private final StoreService storeService;

    /**
     * 최근 5개의 정산 데이터로 상위 3개 메뉴별 판매량 집계
     *
     * @return List<CalculateMenuForGraphDto>
     */
    @Operation(summary = "최근 5영업일 메뉴별 판매량 조회", description = "메뉴 3종류만")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/menu")
    public ResponseEntity<List<CalculateMenuForGraphDto>> getPopularMenus() {
        return ResponseEntity.ok(storeService.getPopularMenus());
    }
}
