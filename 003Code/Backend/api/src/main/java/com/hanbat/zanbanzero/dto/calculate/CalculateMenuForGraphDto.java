package com.hanbat.zanbanzero.dto.calculate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculateMenuForGraphDto {
    private String name;
    private Integer count;

    public static CalculateMenuForGraphDto from(String name, Integer count) {
        return new CalculateMenuForGraphDto(
                name,
                count
        );
    }
}
