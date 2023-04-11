package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private Long id;
    private String name;
    private int cost;
    private String image;
    private Boolean sold;

    public static MenuDto createMenuDto(Menu menu) {
        return new MenuDto(
                menu.getId(),
                menu.getName(),
                menu.getCost(),
                menu.getImage(),
                menu.getSold()
        );
    }
}
