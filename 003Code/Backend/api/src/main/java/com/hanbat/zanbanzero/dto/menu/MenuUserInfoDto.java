package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public static MenuUserInfoDto from(Menu menu) {
        return new MenuUserInfoDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold(),
                menu.getMenuFood().getId(),
                menu.getMenuInfo().getInfo(),
                menu.getMenuInfo().getDetails()
        );
    }
}
