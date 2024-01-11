package com.batch.batch.batch.order.service;

import com.batch.batch.tool.DateTool;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class JobParamService {
    public JobParameters getOrderJobsFirstParameters() {
        return new JobParametersBuilder()
                .addString("date", DateTool.getDate())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("today", DateTool.getToday().toLowerCase())
                .toJobParameters();
    }

    public JobParameters getOrderJobsSecondJobParameters() {
        return new JobParametersBuilder()
                .addString("date", DateTool.getDatePlusOneDay())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("today", DateTool.getDate())
                .addString("day", DateTool.getTodayPlusOneDay().toLowerCase())
                .toJobParameters();
    }

    public JobParameters getPredictWeekJobParameters() {
        return new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
    }

    public JobParameters getStartTodayBatchParameters() {
        return new JobParametersBuilder()
                .addLocalDate("localDate", LocalDate.now())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
    }
}
