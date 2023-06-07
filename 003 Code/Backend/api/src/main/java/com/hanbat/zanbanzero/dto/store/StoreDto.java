package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private String name;
    private String info;

    public static StoreDto of(Store store) {
        return new StoreDto(
                store.getName(),
                store.getInfo()
        );
    }
}
