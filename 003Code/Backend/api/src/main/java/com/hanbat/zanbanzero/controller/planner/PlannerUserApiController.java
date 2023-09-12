package com.hanbat.zanbanzero.controller.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.planner.PlannerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class PlannerUserApiController {

    private final PlannerService service;

    /**
     * 연, 월, 일 식단표 조회
     * @param year - 연
     * @param month - 월
     * @param day - 일
     * @return PlannerDto
     */
    @Operation(summary="일별 식단표 조회", description="n월 n일 하루의 식단표 조회")
    @GetMapping("planner/{year}/{month}/{day}")
    public ResponseEntity<PlannerDto> getOnePlanner(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(service.getOnePlanner(year, month, day));
    }

    /**
     * 연, 월 식단표 조회
     * 
     * @param year - 연
     * @param month - 월
     * @return List<PlannerDto>
     * @throws WrongParameter - month가 1 ~ 12 사이가 아닐 때 발생
     */
    @Operation(summary="월간 식단표 조회", description="n월 한달의 식단표 조회")
    @GetMapping("planner/{year}/{month}")
    public ResponseEntity<List<PlannerDto>> getPlanner(@PathVariable int year, @PathVariable int month) throws WrongParameter {
        if (0 >= month || month > 12) throw new WrongParameter("month(1 ~ 12) : " + month);
        return ResponseEntity.ok(service.getPlanner(year, month));
    }
}
