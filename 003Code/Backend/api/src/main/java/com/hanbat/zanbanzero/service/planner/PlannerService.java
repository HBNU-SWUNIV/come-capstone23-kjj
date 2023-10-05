package com.hanbat.zanbanzero.service.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.planner.PlannerRepository;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository repository;
    private final MenuRepository menuRepository;

    private Menu getPlannerMenu() {
        return menuRepository.findByUsePlanner(true);
    }

    @Transactional
    public PlannerDto setPlanner(PlannerDto dto, int year, int month, int day) {
        LocalDate dateString = DateTools.makeLocalDate(year, month, day);

        Planner planner = repository.findOnePlanner(dateString);
        if (planner == null) {
            dto.setDate(dateString.toString());
            planner = repository.save(Planner.of(dto, getPlannerMenu()));
        }
        else planner.setMenus(dto.getMenus());
        return PlannerDto.of(planner);
    }

    public PlannerDto getOnePlanner(int year, int month, int day) {
        LocalDate date = DateTools.makeLocalDate(year, month, day);
        Planner planner = repository.findOnePlanner(date);
        if (planner == null) return null;

        return PlannerDto.of(planner);
    }

    public List<PlannerDto> getPlanner(int year, int month) {
        LocalDate start = DateTools.makeLocalDate(year, month, 1);
        LocalDate end = DateTools.makeLocalDate(year, month, DateTools.getLastDay(year, month));

        return repository.findAllByDateBetween(start, end).stream()
                .map(PlannerDto::of)
                .toList();
    }
}
