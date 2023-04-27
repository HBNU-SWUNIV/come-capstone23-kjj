package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.entity.store.Calculate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreWeekendDto {
    private String date;
    private int count;

    public static StoreWeekendDto createStoreWeekendDto(Calculate state) {
        return new StoreWeekendDto(
                state.getDate(),
                state.getToday()
        );
    }
}
