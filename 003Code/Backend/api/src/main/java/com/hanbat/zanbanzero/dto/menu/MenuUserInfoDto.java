package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;

public record MenuUserInfoDto(
        Long id,
        String name,
        int cost,
        String image,
        Boolean sold,
        Long foodId,
        String info,
        String details
) {

    public static MenuUserInfoDto from(Menu menu) {
        MenuFood menuFood = menu.getMenuFood();
        MenuInfo menuInfo = menu.getMenuInfo();
        return new MenuUserInfoDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold(),
                (menuFood == null) ? null : menuFood.getId(),
                (menuInfo == null) ? null : menuInfo.getInfo(),
                (menuInfo == null) ? null : menuInfo.getDetails()
        );
    }
}
