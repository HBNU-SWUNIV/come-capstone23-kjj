package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.MenuWithInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuUserInfoDto {
    private Long id;
    private String name;
    private int cost;
    private String image;
    private Boolean sold;
    private Long foodId;

    private String info;
    private String details;

    public static MenuUserInfoDto of(MenuWithInfo menu) {
        return new MenuUserInfoDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold(),
                menu.getFoodId(),
                menu.getInfo(),
                menu.getDetails()
        );
    }
}
