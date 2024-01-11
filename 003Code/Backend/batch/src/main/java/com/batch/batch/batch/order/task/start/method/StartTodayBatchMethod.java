package com.batch.batch.batch.order.task.start.method;

import com.batch.batch.tool.DateTool;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StartTodayBatchMethod {
    public Long getTodayBatchDateId(Connection connection) throws SQLException {
        String getQuery = "select id from batch_date where date = ?;";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, DateTool.getDate());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong("id");
            }
        }
    }
}
