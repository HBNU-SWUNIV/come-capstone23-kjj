package com.hanbat.zanbanzero.dto.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverShow;
import com.hanbat.zanbanzero.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeftoverShowDto {

    private Double leftover;
    private Timestamp updated;

    public static LeftoverShowDto createLeftoverShowDto(LeftoverShow leftoverShow) {
        return new LeftoverShowDto(leftoverShow.getLeftover(), leftoverShow.getUpdated());
    }
}
