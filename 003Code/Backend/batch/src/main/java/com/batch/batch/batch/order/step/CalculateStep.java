package com.batch.batch.batch.order.step;

import com.batch.batch.batch.order.task.CreateCalculatePreTasklet;
import com.batch.batch.batch.order.task.CreateLeftoverPreTasklet;
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
public class CalculateStep {

    private final DataSource dataDataSource;

    public CalculateStep(@Qualifier("dataDataSource") DataSource dataDataSource) {
        this.dataDataSource = dataDataSource;
    }

    @Bean
    public Step createLeftoverPre(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Tasklet tasklet = new CreateLeftoverPreTasklet(dataDataSource);
        return new StepBuilder("createLeftoverPre", jobRepository)
                .tasklet(tasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step createCalculatePre(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Tasklet tasklet = new CreateCalculatePreTasklet(dataDataSource);
        return new StepBuilder("createCalculatePre", jobRepository)
                .tasklet(tasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}