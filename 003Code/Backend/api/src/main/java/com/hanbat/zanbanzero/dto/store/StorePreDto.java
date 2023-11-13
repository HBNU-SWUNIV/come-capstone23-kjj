package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StorePreDto {
    int today;
    int tomorrow;

    public static StorePreDto from(List<Integer> list) {
        return new StorePreDto(
                list.get(0),
                list.get(1)
        );
    }
}
