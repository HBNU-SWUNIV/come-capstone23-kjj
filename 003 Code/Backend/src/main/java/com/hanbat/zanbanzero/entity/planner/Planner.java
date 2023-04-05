package com.hanbat.zanbanzero.entity.planner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp date;
    private String menus;
    private int kcal;

    public static Planner createPlanner(PlannerDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Planner(
                null,
                dto.getDate(),
                dto.getMenus(),
                dto.getKcal()
        );
    }
}
