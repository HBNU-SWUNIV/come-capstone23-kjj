package com.batch.batch.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FoodPredict {
    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;

    public static FoodPredict of(double data) {
        return new FoodPredict(
                data,
                data,
                data,
                data,
                data
        );
    }
}
