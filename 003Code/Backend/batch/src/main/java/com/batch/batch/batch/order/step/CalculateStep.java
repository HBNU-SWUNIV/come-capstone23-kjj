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
public class CalculateStep {

    private final Tasklet createLeftoverPreTasklet;
    private final Tasklet createCalculatePreTasklet;

    public CalculateStep(@Qualifier("createLeftoverPreTasklet") Tasklet createLeftoverPreTasklet, @Qualifier("createCalculatePreTasklet") Tasklet createCalculatePreTasklet) {
        this.createLeftoverPreTasklet = createLeftoverPreTasklet;
        this.createCalculatePreTasklet = createCalculatePreTasklet;
    }

    @Bean
    public Step createLeftoverPre(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createLeftoverPre", jobRepository)
                .tasklet(createLeftoverPreTasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step createCalculatePre(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createCalculatePre", jobRepository)
                .tasklet(createCalculatePreTasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}