package com.hanbat.zanbanzero.controller.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.planner.PlannerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/")
public class PlannerManagerApiController {

    private final PlannerService service;

    @Operation(summary="식단표 관리", description="n월 n일의 식단표 업로드(추가, 수정)")
    @PostMapping("planner/{year}/{month}/{day}")
    public ResponseEntity<String> setPlanner(@RequestBody PlannerDto dto,@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        service.setPlanner(dto, year, month, day);
        return ResponseEntity.ok().body("저장되었습니다.");
    }
}
