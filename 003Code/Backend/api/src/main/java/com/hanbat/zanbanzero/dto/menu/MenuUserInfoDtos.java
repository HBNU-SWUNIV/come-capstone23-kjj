package com.hanbat.zanbanzero.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuUserInfoDtos {
    private List<MenuUserInfoDto> dtos;

    public static MenuUserInfoDtos of(List<MenuUserInfoDto> dtos) {
        return new MenuUserInfoDtos(dtos);
    }
}
