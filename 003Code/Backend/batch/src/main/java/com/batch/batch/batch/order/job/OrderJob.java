package com.batch.batch.batch.order.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderJob {

    private final JobRepository jobRepository;

    @Bean
    public Job countOrdersByDateJob(@Qualifier("createTodayOrderStep") Step step, @Qualifier("countOrdersByDateStep") Step step2)  {
        return new JobBuilder("OrdersJob")
                .repository(jobRepository)
                .start(step)
                .next(step2)
                .build();
    }
}
