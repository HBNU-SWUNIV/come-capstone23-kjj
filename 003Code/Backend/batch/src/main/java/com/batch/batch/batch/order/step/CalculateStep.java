package com.batch.batch.batch.order.step;

import com.batch.batch.batch.order.task.calculate.CreateCalculatePreTasklet;
import com.batch.batch.batch.order.task.calculate.CreateLeftoverPreTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class CalculateStep {

    private final CreateLeftoverPreTasklet createLeftoverPreTasklet;
    private final CreateCalculatePreTasklet createCalculatePreTasklet;

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