package com.batch.batch.batch.order.job.decider;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class OrderJobExecutionDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.FAILED) {
            return FlowExecutionStatus.FAILED;
        }
        return FlowExecutionStatus.COMPLETED;
    }
}
