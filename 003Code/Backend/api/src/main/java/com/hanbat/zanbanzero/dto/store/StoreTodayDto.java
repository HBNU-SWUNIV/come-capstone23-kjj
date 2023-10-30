package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreTodayDto {
    int today;
    int yesterday;

    public static StoreTodayDto of(List<Integer> list) {
        return new StoreTodayDto(
                list.get(0),
                list.get(1)
        );
    }
}
