package com.hanbat.zanbanzero.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuFoodDtos {
    private List<MenuFoodDto> dtos;

    public static MenuFoodDtos from(List<MenuFoodDto> dtos) {
        return new MenuFoodDtos(dtos);
    }
}
