package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeftoverAndPreDto {
    private String date;
    private double real;
    private double predict;

    public static LeftoverAndPreDto of(Leftover leftover, LeftoverPre leftoverPre) {
        return new LeftoverAndPreDto(
                leftoverPre.getCalculate().getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                leftover.getLeftover(),
                leftoverPre.getPredict()
        );
    }
}
