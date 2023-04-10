package com.batch.batch.config;

import com.batch.batch.batch.order.CountOrdersByDateTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;

    @Bean
    public Step countOrdersByDateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Tasklet tasklet = new CountOrdersByDateTasklet(dataSource);
        return new StepBuilder("countOrdersByDateStep", jobRepository)
                // tasklet() : 간단한 단일 일괄 작업 실행, Tasklet 인터페이스 구현체 필요
                // chunk() : 청크 단위로 데이터 조회, 처리, 작성. ItemReader, ItemProcessor, ItemWriter 인터페이스 구현체 필요
                .tasklet(tasklet, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job countOrdersByDateJob(Step step) {
        return new JobBuilder("countOrdersMyDateJob")
                .repository(jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return new CountOrdersByDateTasklet(dataSource);
    }
}