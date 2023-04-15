package com.batch.batch.batch.order.step;

import com.batch.batch.batch.order.tasklet.CountOrdersByDateTasklet;
import com.batch.batch.pojo.Order;
import com.batch.batch.pojo.UserPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.AbstractCursorItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class OrderStep {

    private final DataSource dataDataSource;
    private final JdbcCursorItemReader<UserPolicy> itemReader;
    private final ItemProcessor<UserPolicy, Order> itemProcessor;
    private final ItemWriter<Order> itemWriter;

    public OrderStep(@Qualifier("dataDataSource") DataSource dataDataSource, JdbcCursorItemReader itemReader, ItemProcessor itemProcessor, ItemWriter itemWriter) {
        this.dataDataSource = dataDataSource;
        this.itemReader = itemReader;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;
    }

    @Bean
    public Step createTodayOrderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createTodayOrderStep", jobRepository)
                .<UserPolicy, Order>chunk(5, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
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
