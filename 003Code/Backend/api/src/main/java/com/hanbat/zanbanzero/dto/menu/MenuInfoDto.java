package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
public record MenuInfoDto(
        Long id,
        String name,
        Integer cost,
        String image,
        Boolean sold,
        String info,
        String details) {

    public static MenuInfoDto from(Menu menu) {
        return new MenuInfoDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold(),
                menu.getMenuInfo().getInfo(),
                menu.getMenuInfo().getDetails()
        );
    }
}
