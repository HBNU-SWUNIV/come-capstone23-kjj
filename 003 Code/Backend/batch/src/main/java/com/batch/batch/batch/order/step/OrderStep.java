package com.batch.batch.batch.order.step;

import com.batch.batch.batch.order.tasklet.CountOrdersByDateTasklet;
import com.batch.batch.batch.order.tasklet.CreateTodayOrderTasklet;
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
public class OrderStep {

    private final DataSource dataDataSource;

    public OrderStep(@Qualifier("dataDataSource") DataSource dataSource) {
        dataDataSource = dataSource;
    }

    @Bean
    public Step createTodayOrderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("countOrdersByDateStep", jobRepository)
                .chunk(10, transactionManager)
                .reader(new CreateTodayOrderTasklet().todayUserReader(null))
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step countOrdersByDateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Tasklet tasklet = new CountOrdersByDateTasklet(dataDataSource);
        return new StepBuilder("countOrdersByDateStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}
