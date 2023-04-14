package com.batch.batch.batch.order.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;

@Component
@RequiredArgsConstructor
public class OrderJob {

    private final JobRepository jobRepository;

    @Bean
    public Job countOrdersByDateJob(@Qualifier("createTodayOrderStep") Step step, @Qualifier("countOrdersByDateStep") Step step2)  {
        return new JobBuilder("countOrdersMyDateJob")
                .repository(jobRepository)
                .start(step)
                .next(step2)
                .build();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
