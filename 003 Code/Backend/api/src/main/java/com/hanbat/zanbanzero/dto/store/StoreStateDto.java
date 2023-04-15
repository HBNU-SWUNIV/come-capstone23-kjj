package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.StoreState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreStateDto {

    private String date;
    private Long congestion;
    private int today;

    public static StoreStateDto createStoreStateDto(StoreState storeState) {
        return new StoreStateDto(
                storeState.getDate(),
                storeState.getCongestion(),
                storeState.getToday()
        );
    }
}
