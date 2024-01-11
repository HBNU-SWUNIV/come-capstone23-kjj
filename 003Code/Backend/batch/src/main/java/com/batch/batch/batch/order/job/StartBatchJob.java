package com.batch.batch.batch.order.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartBatchJob {
    private final JobRepository jobRepository;

    @Bean
    public Job startTodayBatchJob(@Qualifier("startTodayBatch") Step step)  {
        return new JobBuilder("StartBatchJob")
                .repository(jobRepository)
                .start(step)
                .build();
    }
}
