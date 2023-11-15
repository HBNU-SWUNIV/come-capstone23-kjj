package com.batch.batch.batch.order.task.calculate.method;

import com.batch.batch.tools.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CreateLeftoverPreMethod {
    public Integer getCalculateData(Connection connection) throws SQLException {
        String date = DateTools.getDate();
        String getQuery = "select today from calculate where date = ? limit 1";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("today");
                } else {
                    log.error(date + " : Calculate data not found");
                    throw new SQLException("Calculate data not found");
                }
            }
        }
    }
}
