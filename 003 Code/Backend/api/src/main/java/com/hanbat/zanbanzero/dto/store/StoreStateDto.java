package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreStateDto {

    private String date;
    private Long congestion;
    private int today;
}
