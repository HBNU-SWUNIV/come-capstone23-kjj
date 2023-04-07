package com.hanbat.zanbanzero.dto.planner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.planner.Planner;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class PlannerDto {
    private Timestamp date;
    private String menus;
    private int kcal;

    public static PlannerDto createPlannerDto(Planner planner){
        ObjectMapper objectMapper = new ObjectMapper();
        return new PlannerDto(
                planner.getDate(),
                planner.getMenus(),
                planner.getKcal()
        );
    }
}
