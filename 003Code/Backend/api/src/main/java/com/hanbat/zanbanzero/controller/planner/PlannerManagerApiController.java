package com.hanbat.zanbanzero.controller.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.service.planner.PlannerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/")
public class PlannerManagerApiController {

    private final PlannerService service;

    /**
     * 연, 월, 일의 식단표 추가, 수정
     *
     * @param dto - date(yyyy-MM-dd), JSON 포맷 menus
     * @param year - 연
     * @param month - 월
     * @param day - 일
     * @return PlannerDto
     */
    @Operation(summary="식단표 관리", description="n월 n일의 식단표 업로드(추가, 수정)")
    @PostMapping("planner/{year}/{month}/{day}")
    public ResponseEntity<PlannerDto> setPlanner(@RequestBody PlannerDto dto,@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(service.setPlanner(dto, year, month, day));
    }
}
