package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuManagerInfoDto {
    private Long id;
    private String name;
    private int cost;
    private String image;
    private Boolean sold;
    private Boolean usePlanner;

    private String info;
    private String details;

    public static MenuManagerInfoDto of(Menu menu, MenuInfo menuInfo) {
        return new MenuManagerInfoDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold(),
                menu.isUsePlanner(),
                menuInfo.getInfo(),
                menuInfo.getDetails()
        );
    }
}
