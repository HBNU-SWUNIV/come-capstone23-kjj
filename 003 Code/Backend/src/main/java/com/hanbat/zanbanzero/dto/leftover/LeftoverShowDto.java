package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverShow;
import com.hanbat.zanbanzero.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeftoverShowDto {

    private Double leftover;
    private String updated;

    public static LeftoverShowDto createLeftoverShowDto(LeftoverShow leftoverShow) {
        return new LeftoverShowDto(leftoverShow.getLeftover(), leftoverShow.getUpdated());
    }
}
