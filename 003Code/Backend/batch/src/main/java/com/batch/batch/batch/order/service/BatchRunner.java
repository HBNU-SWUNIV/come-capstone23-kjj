package com.batch.batch.batch.order.service;

import com.batch.batch.tool.SlackTool;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BatchRunner {
    private final JobLauncher jobLauncher;
    private final SlackTool slackTool;
    private final Job startBatchJob;
    private final JobParamService jobParamService;

    public BatchRunner(JobLauncher jobLauncher, SlackTool slackTool, @Qualifier("startTodayBatchJob") Job startBatchJob, JobParamService jobParamService) {
        this.jobLauncher = jobLauncher;
        this.slackTool = slackTool;
        this.startBatchJob = startBatchJob;
        this.jobParamService = jobParamService;
    }

    public void createTodayBatchDate() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String jobName = "Today's Batch";
        JobExecution run = jobLauncher.run(startBatchJob, jobParamService.getStartTodayBatchParameters());
        if (run.getStatus() != BatchStatus.FAILED) slackTool.sendSlackMessage(jobName);
        else slackTool.sendSlackFailMessage(jobName, run.getStatus().name());
    }
}
