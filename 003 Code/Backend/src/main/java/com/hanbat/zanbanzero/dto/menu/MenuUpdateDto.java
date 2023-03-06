package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuUpdateDto {
    private String name;
    private Integer cost;

    private String info;
    private String details;

    public Menu toMenu() {
        return new Menu(
                null,
                name,
                cost,
                null,
                true
        );
    }

    public MenuInfo toMenuInfo(Menu menu) {
        return new MenuInfo(
                null,
                menu,
                info,
                details
        );
    }
}
