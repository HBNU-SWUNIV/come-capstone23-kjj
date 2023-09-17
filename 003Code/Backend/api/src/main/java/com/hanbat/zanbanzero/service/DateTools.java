package com.hanbat.zanbanzero.service;

import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateTools {
    private DateTools() throws WrongParameter {
        throw new WrongParameter("DateTools can not init");
    }

    public static LocalDate makeTodayToLocalDate() {
        ZonedDateTime date = ZonedDateTime.now().plusHours(9).truncatedTo(ChronoUnit.HOURS);
        return date.toLocalDate();
    }

    public static LocalDate makeLocaldate(int year, int month, int day) {
        Date date = new Date(year - 1900, month - 1, day);
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate makeDateFormatLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static int getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String makeLocaldateToFormatterString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return date.format(formatter);
    }

    public static LocalDate getLastWeeksMonday(int type) throws WrongParameter {

        return switch (type) {
            case 0 ->
                    LocalDateTime.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
            case 1 ->
                    LocalDateTime.now().minusWeeks(2).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
            default -> throw new WrongParameter("type(0 or 1) : " + type);
        };
    }

    public static LocalDate toFormatterLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}
