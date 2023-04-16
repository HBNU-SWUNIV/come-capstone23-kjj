package com.batch.batch.batch.order.tasklet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CountOrdersByDateTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CreateTodayOrderTasklet createTodayOrderTasklet;
    private Map<String, Integer> resultMap = new HashMap<>();

    private String getTodayToString() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    private int countTodayOrders(Connection connection, String date) throws Exception{
        int result = 0;
        Map<Long, String> idToNameMap = createTodayOrderTasklet.getIdToNameMap();

        String getQuery = "select menu, count(*) as count from orders where order_date = ? and recognize = 1 GROUP BY menu;";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long menuId = resultSet.getLong("menu");
                    Integer count = resultSet.getInt("count");
                    result += count;
                    resultMap.put(idToNameMap.get(menuId), count);
                }
            }
        }
        return result;
    }

    private int getSales() {
        int sales = 0;
        Map<String, Integer> nameToCostMap = createTodayOrderTasklet.getNameToCostMap();
        for (String keys : resultMap.keySet()) {
            sales += nameToCostMap.get(keys) * resultMap.get(keys);
        }
        return sales;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = getTodayToString();
        ObjectMapper objectMapper = new ObjectMapper();
        Connection connection = dataSource.getConnection();

        int count = countTodayOrders(connection, date);
        int sales = getSales();

        String insertQuery = "insert into store_state(date, today, all_menus, sales) values(?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, date);
            insertStatement.setInt(2, count);
            insertStatement.setNString(3, objectMapper.writeValueAsString(resultMap));
            insertStatement.setInt(4, sales);
            insertStatement.executeUpdate();
        }

        connection.close();
        return RepeatStatus.FINISHED;
    }
}
