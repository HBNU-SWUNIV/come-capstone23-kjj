package com.hanbat.zanbanzero.service.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.repository.planner.PlannerRepository;
import com.hanbat.zanbanzero.service.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerServiceImplV1 implements PlannerService{

    private final PlannerRepository repository;
    private final DateUtil dateUtil;

    @Override
    @Transactional
    public PlannerDto setPlanner(PlannerDto dto, int year, int month, int day) {
        LocalDate dateString = dateUtil.makeLocalDate(year, month, day);
        dto.setDate(dateString.toString());

        Planner planner = repository.findOnePlanner(dateString).orElse(null);
        if (planner == null) {
            planner = repository.save(Planner.of(dto));
        }
        else planner.setMenus(dto.getMenus());
        return PlannerDto.from(planner);
    }

    @Override
    @Transactional(readOnly = true)
    public PlannerDto getPlannerByDay(int year, int month, int day) {
        LocalDate date = dateUtil.makeLocalDate(year, month, day);

        return repository.findOnePlanner(date)
                .map(PlannerDto::from)
                .orElse(null);
    }
    @Override
    @Transactional(readOnly = true)
    public List<PlannerDto> getPlannerByMonth(int year, int month) {
        LocalDate start = dateUtil.makeLocalDate(year, month, 1);
        LocalDate end = dateUtil.makeLocalDate(year, month, dateUtil.getLastDay(year, month));

        return repository.findAllByDateBetween(start, end).stream()
                .map(PlannerDto::from)
                .toList();
    }
}