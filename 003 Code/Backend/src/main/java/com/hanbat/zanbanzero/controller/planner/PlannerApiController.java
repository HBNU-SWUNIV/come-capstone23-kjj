package com.hanbat.zanbanzero.controller.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.service.planner.PlannerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlannerApiController {

    private final PlannerService service;

    @Operation(summary="식단표 조회", description="n월 한달의 식단표 조회")
    @GetMapping("/api/user/planner/{year}/{month}")
    public ResponseEntity<List<PlannerDto>> getPlanner(@PathVariable int year, @PathVariable int month) {
        List<PlannerDto> result = service.getPlanner(year, month);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary="식단표 업로드", description="n월 한달의 식단표 업로드")
    @PostMapping("/api/user/planner/set/{month}")
    public ResponseEntity<String> setPlanner(@PathVariable int month) {
        service.setPlanner(month);
        return ResponseEntity.ok().body("저장되었습니다.");
    }
}
