package com.batch.batch.batch.order.task.order;

import com.batch.batch.batch.order.task.order.method.CountOrdersByDateMethod;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CountOrdersByDateTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CountOrdersByDateMethod method;
    private final Map<String, Integer> resultMap = new HashMap<>();

    public CountOrdersByDateTasklet(@Qualifier("dataDataSource") DataSource dataSource, CountOrdersByDateMethod method) {
        this.dataSource = dataSource;
        this.method = method;
    }

    private void clear() {
        resultMap.clear();
        CreateTodayOrder.clearNameToCostMap();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = DateTools.getDate();
        Map<String, Integer> nameToCostMap = CreateTodayOrder.getNameToCostMap();

        try (Connection connection = dataSource.getConnection()) {
            int count = method.countTodayOrders(connection, date, resultMap);
            int sales = method.getSales(resultMap);
            Long id = method.initOrder(connection, date, count, sales);
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
        }
        clear();
        return RepeatStatus.FINISHED;
    }
}