package com.batch.batch.batch.order.service;

import com.batch.batch.tools.DateTools;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Service;

@Service
public class JobParamService {
    public JobParameters getOrderJobsFirstParameters() {
        return new JobParametersBuilder()
                .addString("date", DateTools.getDate())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("today", DateTools.getToday().toLowerCase())
                .toJobParameters();
    }

    public JobParameters getOrderJobsSecondJobParameters() {
        return new JobParametersBuilder()
                .addString("date", DateTools.getDatePlusOneDay())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("today", DateTools.getDate())
                .addString("day", DateTools.getTodayPlusOneDay().toLowerCase())
                .toJobParameters();
    }

    public JobParameters getPredictWeekJobParameters() {
        return new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
    }
}
