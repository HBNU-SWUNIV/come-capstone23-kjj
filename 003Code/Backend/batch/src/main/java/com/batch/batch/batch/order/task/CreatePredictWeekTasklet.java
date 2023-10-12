package com.batch.batch.batch.order.task;

import com.batch.batch.tools.ConnectionHandler;
import com.batch.batch.tools.DateTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CreatePredictWeekTasklet implements Tasklet {

    private final DataSource dataSource;
    private final ConnectionHandler connectionHandler;
    private String[] day = {"monday", "tuesday", "wednesday", "thursday", "friday"};

    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        for (String d : day) result.put(d, 0);
        Connection connection = dataSource.getConnection();

        connectionHandler.execute(connection, () -> {
            countOrders(connection, result);
            checkPolicy(connection, result);

            String insertQuery = "insert into calculate_pre_week(date, monday, tuesday, wednesday, thursday, friday) values(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, DateTools.getDate());
                insertStatement.setInt(2, result.get(day[0]));
                insertStatement.setInt(3, result.get(day[1]));
                insertStatement.setInt(4, result.get(day[2]));
                insertStatement.setInt(5, result.get(day[3]));
                insertStatement.setInt(6, result.get(day[4]));
                insertStatement.executeUpdate();
            }
        });
        return RepeatStatus.FINISHED;
    }

    private void countOrders(Connection connection, Map<String, Integer> result) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1); // 다음 주 월요일
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String d : day) {
            String formatted = nextMonday.format(format);

            String getQuery = "select count(*) as count from orders where order_date = ? and recognize = 1;";
            try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
                statement.setString(1, formatted);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.put(d, result.get(d) + resultSet.getInt("count"));
                    }
                }
            }
            nextMonday = nextMonday.plusDays(1);
        }
    }

    private void checkPolicy(Connection connection, Map<String, Integer> result) throws SQLException {
        for (String d : day) {
            String getQuery = "select count(*) as count from user_policy where " + d + " = 1;";
            try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.put(d, result.get(d) + resultSet.getInt("count"));
                    }
                }
            }
        }
    }
}