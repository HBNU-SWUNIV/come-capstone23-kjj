package com.batch.batch.batch.order.controller;

import com.batch.batch.batch.order.service.OrderBatchMethod;
import com.batch.batch.batch.order.service.OrderBatchRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/manual")
@RequiredArgsConstructor
public class OrderBatchController {

    private final OrderBatchRunner batchService;
    private final OrderBatchMethod batchMethod;

    @GetMapping("/predict-week-job")
    public String runPredictWeekJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, SQLException {
        batchService.predictWeekJob();
        return "predictWeekJob success";
    }

    @GetMapping("/order-job")
    public String runOrderJob() throws JobInstanceAlreadyCompleteException, SQLException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        batchMethod.clear();
        batchService.orderJob();
        return "orderJob success";
    }
}
