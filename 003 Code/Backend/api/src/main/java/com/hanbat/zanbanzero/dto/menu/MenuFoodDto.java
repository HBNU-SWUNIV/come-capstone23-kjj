package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.MenuFood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuFoodDto {
    private String food;

    public static MenuFoodDto of(MenuFood menuFood) {
        return new MenuFoodDto(
                menuFood.getFood()
        );
    }
}
