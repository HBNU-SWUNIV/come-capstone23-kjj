package com.batch.batch.batch.order.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class StartBatchStep {
    private final Tasklet startBatchTasklet;

    public StartBatchStep(@Qualifier("startTodayBatchTasklet") Tasklet startBatchTasklet) {
        this.startBatchTasklet = startBatchTasklet;
    }

    @Bean
    public Step startTodayBatch(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("startTodayBatch", jobRepository)
                .tasklet(startBatchTasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}
