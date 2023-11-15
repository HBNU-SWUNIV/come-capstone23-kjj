package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.service.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class StoreWeekendDto {
    private String date;
    private int count;

    public static StoreWeekendDto newZeroDataStoreWeekendDto(LocalDate targetDate) {
        return new StoreWeekendDto(
                DateUtil.makeLocaldateToFormatterString(targetDate),
                0);
    }

    public static StoreWeekendDto of(LocalDate targetDate, int today) {
        return new StoreWeekendDto(
                DateUtil.makeLocaldateToFormatterString(targetDate),
                today
        );
    }
}
