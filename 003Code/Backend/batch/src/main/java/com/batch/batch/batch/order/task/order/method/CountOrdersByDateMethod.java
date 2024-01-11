package com.batch.batch.batch.order.task.order.method;

import com.batch.batch.batch.order.task.order.CreateTodayOrder;
import com.batch.batch.batch.order.task.start.method.StartTodayBatchMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountOrdersByDateMethod {
    private final StartTodayBatchMethod batchMethod;
    public Long initOrder(Connection connection, String date, int today, int sales) throws SQLException {
        Long batchDateId = batchMethod.getTodayBatchDateId(connection);
        String initQuery = "insert into calculate(batch_date_id, today, sales) value(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(initQuery)) {
            statement.setLong(1, batchDateId);
            statement.setInt(2, today);
            statement.setInt(3, sales);
            statement.executeUpdate();
        }
        String getIdQuery = "select id from calculate where date = ?";
        try (PreparedStatement statement = connection.prepareStatement(getIdQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong("id");
            }
        }
    }

    public int countTodayOrders(Connection connection, String date, Map<String, Integer> resultMap) throws SQLException {
        int result = 0;

        String getQuery = "select menu, count(*) as count from orders where order_date = ? and recognize = 1 GROUP BY menu;";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String menu = resultSet.getString("menu");
                    int count = resultSet.getInt("count");
                    result += count;
                    resultMap.put(menu, count);
                }
            }
        }
        return result;
    }

    public int getSales(Map<String, Integer> resultMap) {
        int sales = 0;
        Map<String, Integer> nameToCostMap = CreateTodayOrder.getNameToCostMap();
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            if (!nameToCostMap.containsKey(entry.getKey())) {
                log.warn("TRASH DATA FOUND : name = {}", entry.getKey());
                continue;
            }
            sales += nameToCostMap.get(entry.getKey()) * entry.getValue();
        }

        return sales;
    }
}
