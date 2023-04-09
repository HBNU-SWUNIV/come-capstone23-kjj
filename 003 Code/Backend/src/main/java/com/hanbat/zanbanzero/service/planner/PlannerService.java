package com.hanbat.zanbanzero.service.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.planner.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository repository;

    private String makeDateString(int year, int month, int day) {
        Date date = new Date(year - 1900, month - 1, day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private int getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Transactional
    public void setPlanner(PlannerDto dto, int year, int month, int day) {
        String result = makeDateString(year, month, day);

        Planner planner = repository.findOnePlanner(result);
        if (planner == null) {
            dto.setDate(result);

            repository.save(Planner.createPlanner(dto));
        }
        else {
            planner.setMenus(dto.getMenus());
        }
    }

    public PlannerDto getOnePlanner(int year, int month, int day) {
        String date = makeDateString(year, month, day);
        Planner planner = repository.findOnePlanner(date);
        if (planner == null) return null;

        return PlannerDto.createPlannerDto(planner);
    }

    public List<PlannerDto> getPlanner(int year, int month) {
        if (0 >= month || month > 12) throw new WrongParameter("잘못된 입력입니다.");

        String start = makeDateString(year, month, 1);
        String end = makeDateString(year, month, getLastDay(year, month));

        List<Planner> result = repository.findAllByDateBetween(start, end);
        return result.stream()
                .map(planner -> PlannerDto.createPlannerDto(planner))
                .collect(Collectors.toList());
    }
}
