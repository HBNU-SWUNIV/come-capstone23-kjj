package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuInfoDto {

    private Long id;
    private String name;
    private Integer cost;
    private String image;
    private Boolean sold;

    private String info;
    private String details;

    public static MenuInfoDto createMenuDto(MenuInfo menu) {
        return new MenuInfoDto(
                menu.getId(),
                menu.getMenu().getName(),
                menu.getMenu().getCost(),
                menu.getMenu().getImage(),
                menu.getMenu().getSold(),
                menu.getInfo(),
                menu.getDetails()
        );
    }
}
