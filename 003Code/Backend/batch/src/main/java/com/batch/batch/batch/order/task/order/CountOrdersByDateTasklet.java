package com.batch.batch.batch.order.task.order;

import com.batch.batch.tools.DateTools;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CountOrdersByDateTasklet implements Tasklet {

    private final DataSource dataSource;
    private final Map<String, Integer> resultMap = new HashMap<>();

    public CountOrdersByDateTasklet(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Long initOrder(Connection connection, String date, int today, int sales) throws SQLException {
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
                return resultSet.getLong("id");
            }
        }
    }

    private int countTodayOrders(Connection connection, String date) throws SQLException {
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

    private int getSales() {
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

    private void clear() {
        resultMap.clear();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = DateTools.getDate();
        Map<String, Integer> nameToCostMap = CreateTodayOrder.getNameToCostMap();
        Connection connection = dataSource.getConnection();

        int count = countTodayOrders(connection, date);
        int sales = getSales();
        Long id = initOrder(connection, date, count, sales);
        for (Map.Entry<String, Integer> data : resultMap.entrySet()) {
            String insertQuery = "insert into calculate_menu(calculate_id, menu, count, sales) values(?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setLong(1, id);
                insertStatement.setString(2, data.getKey());
                insertStatement.setInt(3, data.getValue());
                insertStatement.setInt(4, data.getValue() * nameToCostMap.get(data.getKey()));
                insertStatement.executeUpdate();
            }
        }
        clear();

        CreateTodayOrder.clearNameToCostMap();
        return RepeatStatus.FINISHED;
    }
}
