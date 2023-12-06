package com.hanbat.zanbanzero.dto.store;

import java.util.List;

public record StoreTodayDto(
        int today,
        int yesterday
) {
    public static StoreTodayDto from(List<Integer> list) {
        return new StoreTodayDto(
                list.get(0),
                list.get(1)
        );
    }
}
