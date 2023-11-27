package com.hanbat.zanbanzero.dto.menu;

import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuUpdateDto {
    private String name;
    private Integer cost;

    private String info;
    private String details;

    private Boolean usePlanner;

    public MenuInfo from() {
        return new MenuInfo(
                null,
                info,
                details
        );
    }

    public boolean check() {
        if (name == null || cost == null || usePlanner == null) return false;
        return true;
    }
}
