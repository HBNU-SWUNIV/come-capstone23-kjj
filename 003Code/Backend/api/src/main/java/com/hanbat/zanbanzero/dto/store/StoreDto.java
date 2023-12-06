package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.Store;

public record StoreDto(
        String name,
        String info,
        String image
){

    public static StoreDto from(Store store) {
        return new StoreDto(
                store.getName(),
                store.getInfo(),
                store.getImage()
        );
    }
}
