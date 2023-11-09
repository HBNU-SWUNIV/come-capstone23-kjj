package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.service.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreWeekendDto {
    private String date;
    private int count;

    public static StoreWeekendDto createZeroStoreWeekendDto(LocalDate targetDate) {
        return new StoreWeekendDto(
                DateUtil.makeLocaldateToFormatterString(targetDate),
                0);
    }

    public static StoreWeekendDto createStoreWeekendDto(LocalDate targetDate, int today) {
        return new StoreWeekendDto(
                DateUtil.makeLocaldateToFormatterString(targetDate),
                today
        );
    }
}
