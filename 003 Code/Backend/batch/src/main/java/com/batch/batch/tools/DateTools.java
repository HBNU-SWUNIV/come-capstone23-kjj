package com.batch.batch.tools;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateTools {
    public static String getToday() {
        LocalDate date = LocalDate.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.toString();
    }
}
