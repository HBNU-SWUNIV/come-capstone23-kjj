package com.batch.batch.batch.order.task.predictweek;

import com.batch.batch.tools.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Slf4j
@Component
public class CreatePredictWeekTasklet implements Tasklet {

    private final DataSource dataSource;
    private final String[] day = {"monday", "tuesday", "wednesday", "thursday", "friday"};

    public CreatePredictWeekTasklet(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 이번 주 월~금 이용인원 계산
    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        Map<String, List<Long>> doubleCheckMap = new HashMap<>();
        for (String d : day) {
            result.put(d, 0);
            doubleCheckMap.put(d, new ArrayList<>());
        }
        Connection connection = dataSource.getConnection();

        countOrders(connection, result, doubleCheckMap);
        checkPolicy(connection, result, doubleCheckMap);

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
        return RepeatStatus.FINISHED;
    }

    private void countOrders(Connection connection, Map<String, Integer> result, Map<String, List<Long>> doubleCheckMap) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 이번 주 월요일
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String d : day) {
            String formatted = thisMonday.format(format);
            int count = 0;

            String getQuery = "select user_id from orders where order_date = ? and recognize = 1;";
            try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
                statement.setString(1, formatted);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        count += 1;
                        doubleCheckMap.get(d).add(resultSet.getLong("user_id"));
                    }
                }
            }
            result.put(d, count);
            thisMonday = thisMonday.plusDays(1);
        }
    }

    private void checkPolicy(Connection connection, Map<String, Integer> result, Map<String, List<Long>> doubleCheckMap) throws SQLException {
        for (String d : day) {
            String getQuery = "select user_id from user_policy where " + d + " = 1 and default_menu;";
            try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long userId = resultSet.getLong("user_id");
                        if (doubleCheckMap.get(d).contains(userId)) continue;
                        result.put(d, result.get(d) + 1);
                    }
                }
            }
        }
    }
}