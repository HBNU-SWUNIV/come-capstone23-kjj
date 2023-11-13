package com.hanbat.zanbanzero.dto.sbiz;

import com.hanbat.zanbanzero.entity.sbiz.WeeklyFoodPredict;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class WeeklyFoodPredictDto {
    private LocalDate date;

    private EntireData entire;

    private PartData part;

    public static WeeklyFoodPredictDto from(WeeklyFoodPredict predict) {
        return new WeeklyFoodPredictDto(
                predict.getDate(),
                new EntireData(
                        predict.getEntireMonday(),
                        predict.getEntireTuesday(),
                        predict.getEntireWednesday(),
                        predict.getEntireThursday(),
                        predict.getEntireFriday()),
                new PartData(
                        predict.getMonday(),
                        predict.getTuesday(),
                        predict.getWednesday(),
                        predict.getThursday(),
                        predict.getFriday())
        );
    }
}