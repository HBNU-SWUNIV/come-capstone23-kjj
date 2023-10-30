package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreSalesDto {
    int today;
    int yesterday;

    public static StoreSalesDto of(List<Integer> list) {
        return new StoreSalesDto(
                list.get(0),
                list.get(1)
        );
    }
}
