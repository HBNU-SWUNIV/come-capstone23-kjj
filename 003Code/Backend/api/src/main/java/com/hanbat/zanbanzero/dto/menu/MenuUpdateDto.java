package com.hanbat.zanbanzero.dto.menu;

public record MenuUpdateDto(
        String name,
        Integer cost,
        String info,
        String details,
        Boolean usePlanner
) {


    public boolean check() {
        if (name == null || cost == null || usePlanner == null) return false;
        return true;
    }
}
