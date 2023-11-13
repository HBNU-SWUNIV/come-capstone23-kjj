package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreTodayDto {
    int today;
    int yesterday;

    public static StoreTodayDto from(List<Integer> list) {
        return new StoreTodayDto(
                list.get(0),
                list.get(1)
        );
    }
}
