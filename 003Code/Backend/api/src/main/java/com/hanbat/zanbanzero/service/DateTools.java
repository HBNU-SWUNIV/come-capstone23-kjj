package com.hanbat.zanbanzero.service;

import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

public class DateTools {
    private DateTools() throws WrongParameter {
        throw new WrongParameter("DateTools can not init");
    }

    /**
     * openjdk17 이미지 기준 9시간 시차 발생 고려
     * 오늘 날짜 LocalDate로 return
     *
     * @return LocalDate
     */
    public static LocalDate makeTodayToLocalDate() {
        ZonedDateTime date = ZonedDateTime.now().plusHours(9).truncatedTo(ChronoUnit.HOURS);
        return date.toLocalDate();
    }

    /**
     * 연. 월, 일로 LocalDate 객체 생성
     *
     * @param year - 연
     * @param month - 월
     * @param day - 일
     * @return LocalDate
     */
    public static LocalDate makeLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    /**
     * 해당 연, 월의 마지막 일 수 계산
     *
     * @param year - 연
     * @param month - 월
     * @return int
     */
    public static int getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * LocalDate 객체를 yyyyMMdd 포맷의 String으로 변환
     * Response 목적
     *
     * @param date - 변환을 원하는 LocalDate
     * @return String
     */
    public static String makeLocaldateToFormatterString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return date.format(formatter);
    }

    /**
     * type + 1번째 이전 주 월요일 날짜 조회
     *
     * @param type - type + 1번째 전 주 데이터 조회
     * @return LocalDate
     */
    public static LocalDate getLastWeeksMonday(int type) {
        return LocalDateTime.now().minusWeeks(type + 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
    }

    /**
     * yyyy-MM-dd 포맷의 String 객체를 LocalDate로 변환
     *
     * @param date - 변환을 원하는 String 객체
     * @return LocalDate
     */
    public static LocalDate toFormatterLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}
