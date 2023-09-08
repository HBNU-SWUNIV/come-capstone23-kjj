package com.hanbat.zanbanzero.dto.menu;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MenuUserInfoDtos {

    private List<MenuUserInfoDto> dtos = new ArrayList<>();

    public MenuUserInfoDtos(List<MenuUserInfoDto> dtos) {
        this.dtos = dtos;
    }

    public static MenuUserInfoDtos of(List<MenuUserInfoDto> list) {
        return new MenuUserInfoDtos(list);
    }
}
