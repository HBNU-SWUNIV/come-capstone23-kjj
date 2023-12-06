package com.hanbat.zanbanzero.dto.calculate;

public record CalculateMenuForGraphDto(String name, Integer count) {

    public static CalculateMenuForGraphDto from(String name, Integer count) {
        return new CalculateMenuForGraphDto(
                name,
                count
        );
    }
}
