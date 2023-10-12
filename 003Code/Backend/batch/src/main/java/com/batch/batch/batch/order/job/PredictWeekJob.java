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
public class PredictWeekJob {

    private final JobRepository jobRepository;

    @Bean
    public Job createPredictWeekJob(@Qualifier("createPredictWeekData") Step step)  {
        return new JobBuilder("predictWeekJob")
                .repository(jobRepository)
                .start(step)
                .build();
    }
}
