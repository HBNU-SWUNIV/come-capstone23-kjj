package com.batch.batch.batch.order.service;

import com.batch.batch.tool.DateTool;
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

import java.sql.SQLException;

@Service
public class OrderBatchRunner {
    private final JobLauncher jobLauncher;
    private final JobParamService jobParamService;
    private final Job firstJob;
    private final Job secondJob;
    private final Job predictWeekJob;
    private final SlackTool slackTool;
    private final OrderBatchMethod batchMethod;


    public OrderBatchRunner(JobLauncher jobLauncher, JobParamService jobParamService, @Qualifier("countOrdersByDateJob") Job firstJob, @Qualifier("createPredictDataJob") Job secondJob, @Qualifier("createPredictWeekJob") Job predictWeekJob, SlackTool slackTool, OrderBatchMethod batchMethod) {
        this.jobLauncher = jobLauncher;
        this.jobParamService = jobParamService;
        this.firstJob = firstJob;
        this.secondJob = secondJob;
        this.predictWeekJob = predictWeekJob;
        this.slackTool = slackTool;
        this.batchMethod = batchMethod;
    }


    public void orderJob() throws SQLException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String today = DateTool.getToday();
        String date = DateTool.getDate();
        String firstJobName = "OrderJob";
        String secondJobName = "CalculateJob";
        boolean off = batchMethod.isOffDay(date);
        if (!today.equals("SATURDAY") && !today.equals("SUNDAY") && !off) {
            JobExecution run = jobLauncher.run(firstJob, jobParamService.getOrderJobsFirstParameters());
            if (run.getStatus() != BatchStatus.FAILED) {
                slackTool.sendSlackMessage(firstJobName);
                if (!today.equals("FRIDAY")) {
                    JobExecution secondRun = jobLauncher.run(secondJob, jobParamService.getOrderJobsSecondJobParameters());
                    if (secondRun.getStatus() != BatchStatus.FAILED) slackTool.sendSlackMessage(secondJobName);
                }
                else slackTool.sendSlackMessage("Friday! " + secondJobName + " is resting");
            }
            else {
                slackTool.sendSlackFailMessage(firstJobName, run.getStatus().name());
            }
        }
    }

    public void predictWeekJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String jobName = "PredictWeekJob";
        JobExecution run = jobLauncher.run(predictWeekJob, jobParamService.getPredictWeekJobParameters());
        if (run.getStatus() != BatchStatus.FAILED) slackTool.sendSlackMessage(jobName);
        else slackTool.sendSlackFailMessage(jobName, run.getStatus().name());
    }
}
