package com.hanbat.zanbanzero.service;

import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

    public static String makeTodayDateString() {
        ZonedDateTime date = ZonedDateTime.now().plusHours(9).truncatedTo(ChronoUnit.HOURS);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return format.format(date);
    }

    public static String makeResponseDateFormatString(int year, int month, int day) {
        Date date = new Date(year - 1900, month - 1, day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static LocalDate makeResponseDateFormatLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static int getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String makeResponseDateFormatString(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate result = LocalDate.parse(date, inputFormatter);

        return result.format(formatter);
    }

    public static String makeResponseDateFormatString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return date.format(formatter);
    }

    public static LocalDate getLastWeeksMonday(int type) throws WrongParameter {
        LocalDate result;
        switch (type) {
            case 0:
                result = LocalDateTime.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
                break;
            case 1:
                result = LocalDateTime.now().minusWeeks(2).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
                break;
            default:
                throw new WrongParameter("잘못된 타입입니다.");
        }

        return result;
    }

    public static String toFormatterString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }
}
