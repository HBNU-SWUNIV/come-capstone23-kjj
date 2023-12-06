package com.hanbat.zanbanzero.dto.store;

import java.util.List;

public record StorePreDto(
        int today,
        int tomorrow
) {
    public static StorePreDto from(List<Integer> list) {
        return new StorePreDto(
                list.get(0),
                list.get(1)
        );
    }
}
