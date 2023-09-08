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

    private Boolean usePlanner;

    public MenuInfo of(Menu menu) {
        return new MenuInfo(
                null,
                menu,
                info,
                details
        );
    }

    public boolean check() {
        if (name == null || cost == null || details == null || usePlanner == null) {
            return false;
        }
        return true;
    }
}
