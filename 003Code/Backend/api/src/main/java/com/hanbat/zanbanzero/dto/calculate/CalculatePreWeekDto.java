package com.hanbat.zanbanzero.dto.calculate;

import com.hanbat.zanbanzero.entity.calculate.CalculatePreWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculatePreWeekDto {
    private LocalDate date;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;

    public static CalculatePreWeekDto of(CalculatePreWeek data) {
        return new CalculatePreWeekDto(
                data.getDate(),
                data.getMonday(),
                data.getTuesday(),
                data.getWednesday(),
                data.getThursday(),
                data.getFriday()
        );
    }
}
