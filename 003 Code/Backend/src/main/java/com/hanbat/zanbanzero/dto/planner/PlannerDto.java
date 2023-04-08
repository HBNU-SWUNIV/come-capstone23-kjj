package com.hanbat.zanbanzero.dto.planner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.planner.Planner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;

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
