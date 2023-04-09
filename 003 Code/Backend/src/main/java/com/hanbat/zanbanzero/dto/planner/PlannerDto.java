package com.hanbat.zanbanzero.dto.planner;

import com.hanbat.zanbanzero.entity.planner.Planner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlannerDto {
    private String date;
    private String menus;

    public static PlannerDto createPlannerDto(Planner planner){
        return new PlannerDto(
                planner.getDate(),
                planner.getMenus()
        );
    }
}
