package com.batch.batch.batch.order;

import com.batch.batch.batch.order.service.BatchRunner;
import com.batch.batch.batch.order.service.OrderBatchRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {
    private final BatchRunner batchRunner;
    private final OrderBatchRunner orderBatchRunner;

    @Scheduled(cron = "0 30 10 * * ?")
    public void orderJob() throws SQLException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        orderBatchRunner.orderJob();
    }

    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    public void predictWeekJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        orderBatchRunner.predictWeekJob();
    }

    @Scheduled(cron = "0 55 8 * * ?")
    public void createBatchDate() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        batchRunner.createTodayBatchDate();
    }
}