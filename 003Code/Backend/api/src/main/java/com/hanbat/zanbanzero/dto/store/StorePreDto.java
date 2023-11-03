package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorePreDto {
    int today;
    int tomorrow;

    public static StorePreDto of(List<Integer> list) {
        return new StorePreDto(
                list.get(0),
                list.get(1)
        );
    }
}
