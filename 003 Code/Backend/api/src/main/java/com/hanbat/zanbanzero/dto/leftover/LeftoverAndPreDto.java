package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeftoverAndPreDto {
    private String date;
    private double real;
    private double predict;

    public static LeftoverAndPreDto of(Leftover leftover, LeftoverPre leftoverPre) {
        return new LeftoverAndPreDto(
                DateTools.makeResponseDateFormatString(leftoverPre.getCalculate().getDate()),
                leftover.getLeftover(),
                leftoverPre.getPredict()
        );
    }
}
