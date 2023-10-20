package com.batch.batch.batch.order.step;

import com.batch.batch.batch.order.aop.handler.ConnectionHandler;
import com.batch.batch.batch.order.task.predictweek.CreatePredictUserWeekTasklet;
import com.batch.batch.batch.order.task.predictweek.CreatePredictWeekTasklet;
import com.batch.batch.batch.order.aop.handler.ConnectionHandlerV1;
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

    private final DataSource dataDataSource;
    private final ConnectionHandler connectionHandler;

    public PredictWeekStep(@Qualifier("dataDataSource") DataSource dataDataSource, ConnectionHandler connectionHandler) {
        this.dataDataSource = dataDataSource;
        this.connectionHandler = connectionHandler;
    }

    @Bean
    public Step createPredictWeekData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Tasklet tasklet = new CreatePredictWeekTasklet(dataDataSource);
        return new StepBuilder("createPredictWeekData", jobRepository)
                .tasklet(tasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step createPredictUserWeekData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Tasklet tasklet = new CreatePredictUserWeekTasklet(dataDataSource);
        return new StepBuilder("createPredictUserWeekData", jobRepository)
                .tasklet(tasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}