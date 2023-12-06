package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.service.DateUtil;

import java.time.LocalDate;

public record LeftoverDto(String date, Double leftover){

    public static LeftoverDto from(Leftover leftover) {
        return new LeftoverDto(
                DateUtil.makeLocaldateToFormatterString(leftover.getLeftoverPre().getDate()),
                leftover.getData()
        );
    }

    public static LeftoverDto of(LocalDate date, double leftover) {
        return new LeftoverDto(
                DateUtil.makeLocaldateToFormatterString(date),
                leftover
        );
    }
}
