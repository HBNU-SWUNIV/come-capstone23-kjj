package com.hanbat.zanbanzero.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {
    public static String makeDateString(int year, int month, int day) {
        Date date = new Date(year - 1900, month - 1, day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static int getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
