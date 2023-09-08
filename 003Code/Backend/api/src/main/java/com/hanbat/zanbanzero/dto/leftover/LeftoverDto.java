package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeftoverDto {

    private String date;
    private Double leftover;

    public static LeftoverDto of(Leftover leftover) {
        return new LeftoverDto(
                DateTools.makeLocaldateToFormatterString(leftover.getLeftoverPre().getCalculate().getDate()),
                leftover.getLeftover()
        );
    }

    public static LeftoverDto of(LocalDate date, double leftover) {
        return new LeftoverDto(
                DateTools.makeLocaldateToFormatterString(date),
                leftover
        );
    }
}
