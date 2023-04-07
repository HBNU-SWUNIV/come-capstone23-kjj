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
    private Long lat;
    private long lon;

    public StoreDto(Long lat, Long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static StoreDto createStoreDto(Store store) {
        return new StoreDto(
                store.getName(),
                store.getLat(),
                store.getLon()
        );
    }
}
