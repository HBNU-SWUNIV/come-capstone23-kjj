package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.service.DateUtil;

public record StoreStateDto(
        String date,
        String name,
        Boolean off
) {
    public static StoreStateDto from(StoreState state) {
        return new StoreStateDto(
                DateUtil.makeLocaldateToFormatterString(state.getDate()),
                state.getName(),
                state.getOff()
        );
    }
}