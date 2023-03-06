package com.hanbat.zanbanzero.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreStateDto {

    private Long id;
    private Long storeId;
    private Long congestion;
}
