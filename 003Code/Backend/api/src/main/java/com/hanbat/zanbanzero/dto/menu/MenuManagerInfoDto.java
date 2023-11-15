package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuManagerInfoDto {
    private Long id;
    private String name;
    private int cost;
    private String image;
    private Boolean sold;
    private Boolean usePlanner;
    private Long foodId;

    private String info;
    private String details;

    public static MenuManagerInfoDto from(Menu menu) {
        return new MenuManagerInfoDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold(),
                menu.isUsePlanner(),
                menu.getMenuFood().getId(),
                menu.getMenuInfo().getInfo(),
                menu.getMenuInfo().getDetails()
        );
    }
}
