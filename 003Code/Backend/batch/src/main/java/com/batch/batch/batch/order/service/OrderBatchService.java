package com.batch.batch.batch.order.service;

import com.batch.batch.tools.DateTools;
import com.batch.batch.tools.SlackTools;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class OrderBatchService {
    private final JobLauncher jobLauncher;
    private final JobParamService jobParamService;
    private final Job firstJob;
    private final Job secondJob;
    private final Job predictWeekJob;
    private final DataSource dataSource;
    private final SlackTools slackTools;


    public OrderBatchService(JobLauncher jobLauncher, JobParamService jobParamService, @Qualifier("countOrdersByDateJob") Job firstJob, @Qualifier("createPredictDataJob") Job secondJob, @Qualifier("createPredictWeekJob") Job predictWeekJob, @Qualifier("dataDataSource") DataSource dataSource, SlackTools slackTools) {
        this.jobLauncher = jobLauncher;
        this.jobParamService = jobParamService;
        this.firstJob = firstJob;
        this.secondJob = secondJob;
        this.predictWeekJob = predictWeekJob;
        this.dataSource = dataSource;
        this.slackTools = slackTools;
    }


    public void orderJob() throws SQLException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String today = DateTools.getToday();
        String date = DateTools.getDate();
        String firstJobName = "OrderJob";
        String secondJobName = "CalculateJob";
        boolean off = isOffDay(date);
        if (!today.equals("SATURDAY") && !today.equals("SUNDAY") && !off) {
            JobExecution run = jobLauncher.run(firstJob, jobParamService.getOrderJobsFirstParameters());
            if (run.getStatus() != BatchStatus.FAILED) {
                slackTools.sendSlackMessage(firstJobName);
                if (!today.equals("FRIDAY")) {
                    JobExecution secondRun = jobLauncher.run(secondJob, jobParamService.getOrderJobsSecondJobParameters());
                    if (secondRun.getStatus() != BatchStatus.FAILED) slackTools.sendSlackMessage(secondJobName);
                }
                else slackTools.sendSlackMessage("Friday! " + secondJobName + " is resting");
            }
            else {
                slackTools.sendSlackFailMessage(firstJobName, run.getStatus().name());
            }
        }
    }

    private boolean isOffDay(String date) throws SQLException {
        String offQuery = "select exists(select * from store_state where date = ? and off = true)";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(offQuery)) {
                statement.setString(1, date);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) return resultSet.getBoolean(1);
                }
            }
        }
        return false;
    }

    public void predictWeekJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String jobName = "PredictWeekJob";
        JobExecution run = jobLauncher.run(predictWeekJob, jobParamService.getPredictWeekJobParameters());
        if (run.getStatus() != BatchStatus.FAILED) slackTools.sendSlackMessage(jobName);
        else slackTools.sendSlackFailMessage(jobName, run.getStatus().name());
    }

    public void clear() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Long id = getCalculateId(connection);
            if (id == null) return;

            sendDelQuery(connection, id);
        }
    }

    private Long getCalculateId(Connection connection) throws SQLException{
        String getQuery = "select id from calculate where date = ?;";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, DateTools.getDate());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong("id");
            } catch (SQLException e) {
                return null;
            }
        }
    }

    private void sendDelQuery(Connection connection, long id) throws SQLException{
        String delCalculatePreQuery = "delete from calculate_pre where calculate_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delCalculatePreQuery)) {
            statement.setLong(1, id);
            statement.execute();
        }

        String delCalculateMenuQuery = "delete from calculate_menu where calculate_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delCalculateMenuQuery)) {
            statement.setLong(1, id);
            statement.execute();
        }

        String delCalculateQuery = "delete from calculate where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delCalculateQuery)) {
            statement.setLong(1, id);
            statement.execute();
        }
    }
}
