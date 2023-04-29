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
    private Boolean off;

    public static StoreStateDto of(StoreState state) {
        return new StoreStateDto(
                state.getDate(),
                state.getOff()
        );
    }
}
