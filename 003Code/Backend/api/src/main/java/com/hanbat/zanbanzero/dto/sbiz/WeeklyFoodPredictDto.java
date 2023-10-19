package com.hanbat.zanbanzero.dto.sbiz;

import com.hanbat.zanbanzero.entity.sbiz.WeeklyFoodPredict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyFoodPredictDto {
    private Timestamp date;

    private EntireData entire;

    private PartData part;

    public static WeeklyFoodPredictDto of(WeeklyFoodPredict predict) {
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