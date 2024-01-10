package com.hanbat.zanbanzero.controller.leftover;

import com.hanbat.zanbanzero.aop.annotation.RestControllerClass;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.leftover.LeftoverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "잔반 컨트롤러", description = "현재 미사용")
@RequiredArgsConstructor
@RestControllerClass("/api/manager/leftover")
public class LeftoverApiController {

    private final LeftoverService leftoverService;

    /**
     * 이전 주 월 ~ 금 잔반 발생량 조회
     * 데이터가 없을 경우 잔반량은 0.0
     *
     * @param type - 원하는 주의 -1 값 (ex. 1주 전 데이터일 경우 type = 0)
     * @return List<LeftoverDto>
     * @throws WrongParameter - type 음수일 경우 발생
     */
    @Operation(summary="지난주 잔반량 데이터 조회", description="월~금 5개 데이터")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공"),
            @ApiResponse(responseCode = "400", description = "type이 음수인 경우")
    })
    @GetMapping("/last-week/{type}")
    public ResponseEntity<List<LeftoverDto>> getLastWeeksLeftovers(@PathVariable int type) throws WrongParameter {
        if (type < 0) throw new WrongParameter("type(0 ~) : " + type);
        return ResponseEntity.ok(leftoverService.getLastWeeksLeftovers(type));
    }
}
