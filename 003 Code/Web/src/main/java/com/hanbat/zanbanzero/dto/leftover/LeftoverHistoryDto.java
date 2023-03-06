package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeftoverHistoryDto {

    private Long id;
    private String date;
    private Double leftover;

    public static LeftoverHistoryDto createLeftoverHistoryDto(LeftoverHistory leftoverHistory) {
        return new LeftoverHistoryDto(
                leftoverHistory.getId(),
                leftoverHistory.getDate(),
                leftoverHistory.getLeftover()
        );
    }
}
