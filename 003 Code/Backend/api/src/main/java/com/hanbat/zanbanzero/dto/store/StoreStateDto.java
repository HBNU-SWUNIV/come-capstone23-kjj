package com.hanbat.zanbanzero.dto.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.store.StoreState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreStateDto {

    private String date;
    private int today;
    private Map<String, Integer> allMenus;
    private int sales;

    public static StoreStateDto createStoreStateDto(StoreState storeState) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new StoreStateDto(
                storeState.getDate(),
                storeState.getToday(),
                objectMapper.readValue(storeState.getAllMenus(), new TypeReference<>() {}),
                storeState.getSales()
        );
    }
}
