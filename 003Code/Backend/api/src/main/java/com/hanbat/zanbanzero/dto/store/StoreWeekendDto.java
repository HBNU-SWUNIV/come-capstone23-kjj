package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.service.DateUtil;

import java.time.LocalDate;

public record StoreWeekendDto(
        String date,
        int count
) {
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
