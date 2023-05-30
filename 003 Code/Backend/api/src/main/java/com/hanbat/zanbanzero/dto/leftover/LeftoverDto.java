package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeftoverDto {

    private String date;
    private Double leftover;

    public static LeftoverDto of(Leftover leftover) {
        return new LeftoverDto(
                DateTools.makeResponseDateFormatString(leftover.getLeftoverPre().getCalculate().getDate()),
                leftover.getLeftover()
        );
    }
}
