package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;

public record MenuDto(Long id, String name, int cost, String image, Boolean sold) {

    public static MenuDto from(Menu menu) {
        return new MenuDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold()
        );
    }
}
