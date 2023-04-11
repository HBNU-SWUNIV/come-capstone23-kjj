package com.batch.batch.batch.order;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
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
    @Scheduled(cron = "0 22 20 * * ?")
    public void runOrderJob() throws Exception {
        jobLauncher.run(job, new JobParameters());
    }

}
