package com.hanbat.zanbanzero.dto.calculate;

import com.hanbat.zanbanzero.entity.calculate.CalculateMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateMenuForGraphDto {
    private String name;
    private long count;

    public static CalculateMenuForGraphDto createCalculateMenuForGraphDto(CalculateMenu menu) {
        return new CalculateMenuForGraphDto(
                menu.getMenu(),
                menu.getCount()
        );
    }
}
