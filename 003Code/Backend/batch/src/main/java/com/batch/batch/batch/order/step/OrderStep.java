package com.batch.batch.batch.order.step;

import com.batch.batch.entity.Order;
import com.batch.batch.entity.UserPolicy;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class OrderStep {
    private final JdbcCursorItemReader<UserPolicy> itemReader;
    private final ItemProcessor<UserPolicy, Order> itemProcessor;
    private final ItemWriter<Order> itemWriter;

    private final Tasklet countOrdersByDateTasklet;

    public OrderStep(JdbcCursorItemReader itemReader, ItemProcessor itemProcessor, ItemWriter itemWriter, @Qualifier("countOrdersByDateTasklet") Tasklet countOrdersByDateTasklet) {
        this.itemReader = itemReader;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;
        this.countOrdersByDateTasklet = countOrdersByDateTasklet;
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
        return new StepBuilder("countOrdersByDateStep", jobRepository)
                .tasklet(countOrdersByDateTasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}
