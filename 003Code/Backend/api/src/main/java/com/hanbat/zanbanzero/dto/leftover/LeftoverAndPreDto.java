package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class LeftoverAndPreDto {
    private String date;
    private double real;
    private double predict;

    public static LeftoverAndPreDto of(Leftover leftover, LeftoverPre leftoverPre) {
        return new LeftoverAndPreDto(
                leftoverPre.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                leftover.getData(),
                leftoverPre.getPredict()
        );
    }
}
