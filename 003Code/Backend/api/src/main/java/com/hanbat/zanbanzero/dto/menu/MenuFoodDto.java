package com.hanbat.zanbanzero.dto.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.menu.MenuFood;

import java.util.HashMap;

public record MenuFoodDto(Long id, String name, HashMap<String, Integer> food) {

    public static MenuFoodDto from(MenuFood menuFood) {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Integer> food;
        try {
            food = objectMapper.readValue(menuFood.getFood(), HashMap.class);
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
