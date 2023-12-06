package com.hanbat.zanbanzero.dto.menu;

import java.util.List;

public record MenuUserInfoDtos(
        List<MenuUserInfoDto> dtos
) {

    public static MenuUserInfoDtos from(List<MenuUserInfoDto> dtos) {
        return new MenuUserInfoDtos(dtos);
    }
}
