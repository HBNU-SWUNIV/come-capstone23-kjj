package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;

import java.time.format.DateTimeFormatter;

public record LeftoverAndPreDto(String date, double real, double predict) {

    public static LeftoverAndPreDto of(Leftover leftover, LeftoverPre leftoverPre) {
        return new LeftoverAndPreDto(
                leftoverPre.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                leftover.getData(),
                leftoverPre.getPredict()
        );
    }
}
