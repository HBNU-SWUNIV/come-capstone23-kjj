package com.batch.batch.batch.order.job;

import com.batch.batch.batch.order.job.decider.OrderJobExecutionDecider;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderJob {

    private final JobRepository jobRepository;
    private final OrderJobExecutionDecider orderJobExecutionDecider;

    @Bean
    public Job countOrdersByDateJob(@Qualifier("createTodayOrderStep") Step step, @Qualifier("countOrdersByDateStep") Step step2, @Qualifier("createLeftoverPre") Step step3)  {
        return new JobBuilder("OrdersJob")
                .repository(jobRepository)
                .start(step)
                .next(orderJobExecutionDecider).on(FlowExecutionStatus.COMPLETED.getName()).to(step2)
                .next(step3)
                .end()
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
