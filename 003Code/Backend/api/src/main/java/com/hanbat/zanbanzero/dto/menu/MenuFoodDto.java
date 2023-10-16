package com.hanbat.zanbanzero.dto.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuFoodDto {
    private Long id;
    private String name;
    private Map<String, Integer> food;

    public static MenuFoodDto of(MenuFood menuFood) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map food;
        try {
            food = objectMapper.readValue(menuFood.getFood(), Map.class);
        } catch (JsonProcessingException e) {
            food = null;
        }
        return new MenuFoodDto(
                menuFood.getId(),
                menuFood.getName(),
                food
        );
    }
}
