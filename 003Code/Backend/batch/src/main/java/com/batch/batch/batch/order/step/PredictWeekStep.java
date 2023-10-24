package com.batch.batch.batch.order.step;

import com.batch.batch.batch.order.task.predictweek.CreatePredictUserWeekTasklet;
import com.batch.batch.batch.order.task.predictweek.CreatePredictWeekTasklet;
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
public class PredictWeekStep {
    private final CreatePredictWeekTasklet createPredictWeekTasklet;
    private final CreatePredictUserWeekTasklet createPredictUserWeekTasklet;

    public PredictWeekStep(CreatePredictWeekTasklet createPredictWeekTasklet, CreatePredictUserWeekTasklet createPredictUserWeekTasklet) {
        this.createPredictWeekTasklet = createPredictWeekTasklet;
        this.createPredictUserWeekTasklet = createPredictUserWeekTasklet;
    }

    @Bean
    public Step createPredictWeekData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createPredictWeekData", jobRepository)
                .tasklet(createPredictWeekTasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step createPredictUserWeekData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createPredictUserWeekData", jobRepository)
                .tasklet(createPredictUserWeekTasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}