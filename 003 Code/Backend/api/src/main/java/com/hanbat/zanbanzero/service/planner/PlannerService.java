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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository repository;
    private final MenuRepository menuRepository;

    private Menu getPlannerMenu() {
        return menuRepository.findByUsePlanner(true);
    }

    @Transactional
    public void setPlanner(PlannerDto dto, int year, int month, int day) {
        String dateString = DateTools.makeDateFormatString(year, month, day);

        Planner planner = repository.findOnePlanner(dateString);
        if (planner == null) {
            dto.setDate(dateString);
            repository.save(Planner.of(dto, getPlannerMenu()));
        }
        else planner.setMenus(dto.getMenus());

    }

    public PlannerDto getOnePlanner(int year, int month, int day) {
        String date = DateTools.makeDateFormatString(year, month, day);
        Planner planner = repository.findOnePlanner(date);
        if (planner == null) return null;

        return PlannerDto.of(planner);
    }

    public List<PlannerDto> getPlanner(int year, int month) {
        String start = DateTools.makeDateFormatString(year, month, 1);
        String end = DateTools.makeDateFormatString(year, month, DateTools.getLastDay(year, month));

        return repository.findAllByDateBetween(start, end).stream()
                .map(planner -> PlannerDto.of(planner))
                .collect(Collectors.toList());
    }
}
