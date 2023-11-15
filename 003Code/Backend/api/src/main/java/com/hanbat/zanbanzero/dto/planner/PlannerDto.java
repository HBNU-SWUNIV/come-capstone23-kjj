package com.hanbat.zanbanzero.dto.planner;

import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.service.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlannerDto {
    private String date;
    private String menus;

    public static PlannerDto from(Planner planner){
        return new PlannerDto(
                DateUtil.makeLocaldateToFormatterString(planner.getDate()),
                planner.getMenus()
        );
    }
}
