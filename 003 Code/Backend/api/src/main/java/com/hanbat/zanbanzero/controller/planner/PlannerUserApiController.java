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

    @Operation(summary="일별 식단표 조회", description="n월 n일 하루의 식단표 조회")
    @GetMapping("planner/{year}/{month}/{day}")
    public ResponseEntity<PlannerDto> getOnePlanner(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(service.getOnePlanner(year, month, day));
    }

    @Operation(summary="월간 식단표 조회", description="n월 한달의 식단표 조회")
    @GetMapping("planner/{year}/{month}")
    public ResponseEntity<List<PlannerDto>> getPlanner(@PathVariable int year, @PathVariable int month) throws WrongParameter {
        if (0 >= month || month > 12) throw new WrongParameter("잘못된 입력입니다.");
        return ResponseEntity.ok(service.getPlanner(year, month));
    }
}
