package com.batch.batch.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
public class FoodPredict {
    private Timestamp date;

    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;
}
