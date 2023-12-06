package com.hanbat.zanbanzero.dto.planner;

import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.service.DateUtil;

public record PlannerDto(
        String date,
        String menus   
) {

    public static PlannerDto from(Planner planner){
        return new PlannerDto(
                DateUtil.makeLocaldateToFormatterString(planner.getDate()),
                planner.getMenus()
        );
    }
}
