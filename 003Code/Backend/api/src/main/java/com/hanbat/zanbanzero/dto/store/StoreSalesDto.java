package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreSalesDto {
    int today;
    int yesterday;

    public static StoreSalesDto from(List<Integer> list) {
        return new StoreSalesDto(
                list.get(0),
                list.get(1)
        );
    }
}
