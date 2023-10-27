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
    public Map<String, Integer> getCalculateData(Connection connection) throws SQLException {
        Map<String, Integer> result = new HashMap<>();
        String date = DateTools.getDate();
        String getQuery = "select id, today from calculate where date = ? limit 1";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
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
}
