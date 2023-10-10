package com.hanbat.zanbanzero.service.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;

import java.util.List;

public interface PlannerService {
    PlannerDto setPlanner(PlannerDto dto, int year, int month, int day);

    PlannerDto getPlannerByDay(int year, int month, int day);

    List<PlannerDto> getPlannerByMonth(int year, int month);
}
