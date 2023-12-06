package com.hanbat.zanbanzero.dto.store;

import java.util.List;

public record StoreSalesDto(
        int today,
        int yesterday
) {
    public static StoreSalesDto from(List<Integer> list) {
        return new StoreSalesDto(
                list.get(0),
                list.get(1)
        );
    }
}
