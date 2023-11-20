package com.batch.batch.batch.order.task.predictweek.method;

import com.batch.batch.tools.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CreatePredictWeekMethod {

    public void countOrders(Connection connection, Map<String, Integer> result, Map<String, LinkedList<Long>> doubleCheckMap) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 이번 주 월요일
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String d : DateTools.getDayArray()) {
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

    public void checkPolicy(Connection connection, Map<String, Integer> result, Map<String, LinkedList<Long>> doubleCheckMap) throws SQLException {
        for (String d : DateTools.getDayArray()) {
            String getQuery = "select u.id as id from user as u join user_policy as up on u.user_policy_id = up.id where up." + d + " = 1 and default_menu;";
            try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long userId = resultSet.getLong("id");
                        if (doubleCheckMap.get(d).contains(userId)) continue;
                        result.put(d, result.get(d) + 1);
                    }
                }
            }
        }
    }
}