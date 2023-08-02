package com.hanbat.zanbanzero.dto.store;

import com.hanbat.zanbanzero.service.DateTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreWeekendDto {
    private String date;
    private int count;

    public static StoreWeekendDto createZeroStoreWeekendDto(String targetDate) {
        return new StoreWeekendDto(
                DateTools.makeDateFormatString(targetDate),
                0);
    }

    public static StoreWeekendDto createStoreWeekendDto(String targetDate, int today) {
        return new StoreWeekendDto(
                DateTools.makeDateFormatString(targetDate),
                today
        );
    }
}
