package com.batch.batch.batch.order;

import com.batch.batch.tools.DateTools;
import com.batch.batch.tools.SlackTools;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job firstJob;
    private final Job secondJob;
    private final DataSource dataSource;

    private final SlackTools slackTools;

    public BatchScheduler(JobLauncher jobLauncher, @Qualifier("countOrdersByDateJob") Job firstJob, @Qualifier("createPredictDataJob") Job secondJob, @Qualifier("dataDataSource") DataSource dataSource, SlackTools slackTools) {
        this.jobLauncher = jobLauncher;
        this.firstJob = firstJob;
        this.secondJob = secondJob;
        this.dataSource = dataSource;
        this.slackTools = slackTools;
    }

    private JobParameters getFirstJobParameters() {
        return new JobParametersBuilder()
                .addString("date", DateTools.getDate())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("today", DateTools.getToday().toLowerCase())
                .toJobParameters();
    }

    private JobParameters getSecondJobParameters() {
        return new JobParametersBuilder()
                .addString("date", DateTools.getDatePlusOneDay())
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("today", DateTools.getDate())
                .addString("day", DateTools.getTodayPlusOneDay().toLowerCase())
                .toJobParameters();
    }

    @Scheduled(cron = "0 30 1 * * ?")
    public void runOrderJob() throws SQLException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String today = DateTools.getToday();
        String date = DateTools.getDate();
        boolean off = isOffDay(date);
        if (!today.equals("SATURDAY") && !today.equals("SUNDAY") && !off) {
            JobExecution run = jobLauncher.run(firstJob, getFirstJobParameters());
            if (run.getStatus() != BatchStatus.FAILED && !today.equals("FRIDAY")) {
                slackTools.sendSlackMessage("firstJob");
                JobExecution secondRun = jobLauncher.run(secondJob, getSecondJobParameters());
                if (secondRun.getStatus() != BatchStatus.FAILED) slackTools.sendSlackMessage("secondJob");

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
}
