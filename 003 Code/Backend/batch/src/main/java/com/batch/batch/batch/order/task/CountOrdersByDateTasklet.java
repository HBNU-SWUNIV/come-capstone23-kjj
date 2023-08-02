package com.batch.batch.batch.order.task;

import com.batch.batch.tools.ConnectionHandler;
import com.batch.batch.tools.DateTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CountOrdersByDateTasklet implements Tasklet {

    private final ConnectionHandler connectionHandler;
    private final DataSource dataSource;
    private final CreateTodayOrder createTodayOrder;
    private Map<String, Integer> resultMap = new HashMap<>();

    private Long initOrder(Connection connection, String date, int today, int sales) throws SQLException{
        Long id;

        String initQuery = "insert into calculate(date, today, sales) value(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(initQuery)) {
            statement.setString(1, date);
            statement.setInt(2, today);
            statement.setInt(3, sales);
            statement.executeUpdate();
        }
        String getIdQuery = "select id from calculate where date = ?";
        try (PreparedStatement statement = connection.prepareStatement(getIdQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id = resultSet.getLong("id");
            }
        }
        return id;
    }

    private int countTodayOrders(Connection connection, String date) throws SQLException {
        int result = 0;

        String getQuery = "select menu, count(*) as count from orders where order_date = ? and recognize = 1 GROUP BY menu;";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String menu = resultSet.getString("menu");
                    Integer count = resultSet.getInt("count");
                    result += count;
                    resultMap.put(menu, count);
                }
            }
        }
        return result;
    }

    private int getSales() {
        int sales = 0;
        Map<String, Integer> nameToCostMap = createTodayOrder.getNameToCostMap();
        for (String keys : resultMap.keySet()) {
            sales += nameToCostMap.get(keys) * resultMap.get(keys);
        }
        return sales;
    }

    private void clear() {
        resultMap.clear();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = DateTools.getDate();
        Map<String, Integer> nameToCostMap = createTodayOrder.getNameToCostMap();
        Connection connection = dataSource.getConnection();

        connectionHandler.execute(connection, () -> {
            int count = countTodayOrders(connection, date);
            int sales = getSales();
            Long id = initOrder(connection, date, count, sales);
            for (String key : resultMap.keySet()) {
                String insertQuery = "insert into calculate_menu(calculate_id, menu, count, sales) values(?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setLong(1, id);
                    insertStatement.setString(2, key);
                    insertStatement.setInt(3, resultMap.get(key));
                    insertStatement.setInt(4, resultMap.get(key) * nameToCostMap.get(key));
                    insertStatement.executeUpdate();
                }
            }
            clear();
        });

        return RepeatStatus.FINISHED;
    }
}
