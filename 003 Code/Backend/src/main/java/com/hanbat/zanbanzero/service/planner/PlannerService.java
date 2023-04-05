package com.hanbat.zanbanzero.service.planner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.repository.planner.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository repository;

    public List<PlannerDto> getPlanner(int year, int month) {
        Timestamp start = new Timestamp(year - 1900, month - 1, 1, 0, 0, 0, 0);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Timestamp end = new Timestamp(year - 1900, month - 1, lastDay, 23, 59, 59, 999999999);
        List<Planner> result = repository.findAllByDateBetween(start, end);
        return result.stream()
                .map(entity -> PlannerDto.createPlannerDto(entity))
                .collect(Collectors.toList());
    }

    public List<PlannerDto> setPlanner(int month) {
        return null;
    }
}
