package com.batch.batch.batch.order;

import com.batch.batch.batch.order.service.OrderBatchService;
import com.batch.batch.tools.DateTools;
import com.batch.batch.tools.SlackTools;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {
    private final OrderBatchService orderBatchService;

    @Scheduled(cron = "0 30 10 * * ?")
    public void orderJob() throws SQLException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        orderBatchService.orderJob();
    }

    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    public void predictWeekJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        orderBatchService.predictWeekJob();
    }
}