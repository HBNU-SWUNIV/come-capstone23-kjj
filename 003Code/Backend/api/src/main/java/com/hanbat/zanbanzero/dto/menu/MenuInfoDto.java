package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuInfoDto {

    private Long id;
    private String name;
    private Integer cost;
    private String image;
    private Boolean sold;

    private String info;
    private String details;

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
