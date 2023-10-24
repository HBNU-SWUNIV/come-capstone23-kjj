package com.batch.batch.batch.order.task.calculate;

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
public class CreateLeftoverPreTasklet implements Tasklet {

    private final DataSource dataSource;

    public CreateLeftoverPreTasklet(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Map<String, Object> getCalculateData(Connection connection) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        String date = DateTools.getDate();
        String getQuery = "select id, today from calculate where date = ? limit 1";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    int today = resultSet.getInt("today");

                    result.put("id", id);
                    result.put("today", today);
                }
                else {
                    log.error(date + " : Calculate data not found");
                }
            }
        }
        return result;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Connection connection = dataSource.getConnection();

        Map<String, Object> calculateData = getCalculateData(connection);
        Long id = (Long) calculateData.get("id");
        int today = (int) calculateData.get("today");

        String query = "insert into leftover_pre(calculate_id, predict) values(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            double aver = 120;
            statement.setDouble(2, aver * today);

            statement.executeUpdate();
        }

        connection.close();
        return RepeatStatus.FINISHED;
    }
}
