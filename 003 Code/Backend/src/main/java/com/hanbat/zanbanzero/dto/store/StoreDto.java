package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private Long id;
    private Long managerId;
    private Long lat;
    private long lon;

    public static StoreDto createStoreDto(Store store) {
        return new StoreDto(
                store.getId(),
                store.getManager().getId(),
                store.getLat(),
                store.getLon()
        );
    }
}
