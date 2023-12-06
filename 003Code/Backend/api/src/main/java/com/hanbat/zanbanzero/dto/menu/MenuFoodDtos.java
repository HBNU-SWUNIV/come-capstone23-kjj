package com.hanbat.zanbanzero.dto.menu;

import java.util.List;

public record MenuFoodDtos(List<MenuFoodDto> dtos) {
    public static MenuFoodDtos from(List<MenuFoodDto> dtos) {
        return new MenuFoodDtos(dtos);
    }
}
