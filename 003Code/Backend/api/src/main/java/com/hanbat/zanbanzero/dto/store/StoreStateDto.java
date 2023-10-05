package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreStateDto {
    private String date;
    private String name;
    private Boolean off;

    public static StoreStateDto of(StoreState state) {
        return new StoreStateDto(
                DateTools.makeLocaldateToFormatterString(state.getDate()),
                state.getName(),
                state.getOff()
        );
    }
}