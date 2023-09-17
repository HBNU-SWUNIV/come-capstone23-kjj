package com.batch.batch.batch.order.job;

import com.batch.batch.batch.order.job.decider.OrderJobExecutionDecider;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CalculateJob {

    private final JobRepository jobRepository;

    @Bean
    public Job createPredictDataJob(@Qualifier("createLeftoverPre") Step step, @Qualifier("createCalculatePre") Step step2)  {
        return new JobBuilder("CalculateJob")
                .repository(jobRepository)
                .start(step)
                .next(step2)
                .build();
    }
}
