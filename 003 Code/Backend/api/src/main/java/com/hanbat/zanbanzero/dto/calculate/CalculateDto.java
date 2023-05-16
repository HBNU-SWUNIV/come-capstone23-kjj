package com.hanbat.zanbanzero.dto.calculate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateDto {

    private String date;

    public static CalculateDto createStoreStateDto(Calculate calculate) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new CalculateDto(
                calculate.getDate()
        );
    }
}
