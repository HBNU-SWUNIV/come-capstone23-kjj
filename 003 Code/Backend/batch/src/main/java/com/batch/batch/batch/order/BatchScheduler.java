package com.batch.batch.batch.order;

import com.batch.batch.tools.DateTools;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job job;

    // Docker image(openjdk:17) 기준 한국이 9시간 느림
    @Scheduled(cron = "0 30 1 * * ?")
    public void runOrderJob() throws Exception {
        String day = DateTools.getToday();
        String date = DateTools.getDate();
        if (!day.equals("SATURDAY") && !day.equals("SUNDAY")) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("date", date)
                    //.addString("today", "friday")
                    //.addString("time", String.valueOf(System.currentTimeMillis()))
                    .addString("today", day.toLowerCase())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        }
    }
}
