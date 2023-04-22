package com.batch.batch.batch.order.tasklet;

import com.batch.batch.tools.DateTools;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CountOrdersByDateTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CreateTodayOrderTasklet createTodayOrderTasklet;
    private Map<String, Integer> resultMap = new HashMap<>();

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

    private void clear() {
        resultMap.clear();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = DateTools.getDate();
        ObjectMapper objectMapper = new ObjectMapper();
        Connection connection = dataSource.getConnection();

        int count = countTodayOrders(connection, date);
        int sales = getSales();
        log.info("정산 tasklet run : " + date);

        String insertQuery = "insert into calculate(date, today, all_menus, sales) values(?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, date);
            insertStatement.setInt(2, count);
            insertStatement.setNString(3, objectMapper.writeValueAsString(resultMap));
            insertStatement.setInt(4, sales);
            insertStatement.executeUpdate();
        }

        connection.close();
        clear();
        return RepeatStatus.FINISHED;
    }
}
