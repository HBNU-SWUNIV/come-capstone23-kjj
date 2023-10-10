package com.batch.batch.batch.order.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CreateCalculatePreTasklet implements Tasklet {

    private final DataSource dataSource;
    private static final Map<Long, String> menuIdToNameMap = new HashMap<>();
    private static final Map<String, Long> menuNameToIdMap = new HashMap<>();

    private Long calculateCheck(Connection connection, String today, String day) throws SQLException {
        if (day.equals("monday")) {
            String query = "insert into calculate(date, today, sales) values(?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, today);
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                statement.executeUpdate();
            }
        }
        String query = "select id from calculate where date = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, today);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong("id");
            }
        }
        return null;
    }

    private void initMenu(Connection connection) throws SQLException {
        String query = "select id, name from menu";
        try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                menuIdToNameMap.put(id, name);
                menuNameToIdMap.put(name, id);
            }
        }
    }

    private Map<String, Integer> getUserPolicy(Connection connection, String day) throws SQLException {
        Map<String, Integer> result = new HashMap<>();
        String query = "select default_menu, COUNT(*) as count from user_policy where " + day + " = 1 and default_menu group by default_menu";

        try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
            while (resultSet.next()) {
                String defaultMenu = menuIdToNameMap.get(resultSet.getLong("default_menu"));
                int count = resultSet.getInt("count");
                result.put(defaultMenu, count);
            }
        }
        return result;
    }

    private void checkOrders(Connection connection, String date, Map<String, Integer> result) throws SQLException {
        String query = "select id, menu, recognize from orders where order_date = ?";

        List<Long> updateRecognizeIds = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String menu = resultSet.getString("menu");
                int type = resultSet.getInt("recognize");
                if (!result.containsKey(menu)) updateRecognizeIds.add(id);
                else {
                    if (type == 1) result.put(menu, result.get(menu) + 1);
                    else result.put(menu, result.get(menu) - 1);
                }
            }
        }
        updateOrderRecognize(connection, updateRecognizeIds);
    }

    private void updateOrderRecognize(Connection connection, List<Long> ids) throws SQLException {
        for (Long id : ids) {
            String query = "update orders set recognize = 0 where id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        }
    }

    private Map<String, Integer> getPredictFood(Connection connection, Map<String, Integer> map) throws SQLException, JsonProcessingException {
        Map<String, Integer> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String query = "select menu_id, food from menu_food";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("menu_id");
                String menuName = menuIdToNameMap.get(id);
                if (map.get(menuName) == null || map.get(menuName) == 0) continue;

                String menu = resultSet.getString("food");
                Map<String, Integer> foods = objectMapper.readValue(menu, Map.class);
                for (Map.Entry<String, Integer> entry : foods.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (result.containsKey(key)) result.put(key, result.get(key) + value * map.get(menuName));
                    else result.put(key, value * map.get(menuName));
                }
            }
        }

        return result;
    }

    private void savePredictData(Connection connection, Long id, int predictUser, String predictFood, String predictMenu) throws SQLException {
        String query = "insert into calculate_pre(calculate_id, predict_user, predict_food, predict_menu) values(?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.setInt(2, predictUser);
            statement.setString(3, predictFood);
            statement.setString(4, predictMenu);
            statement.executeUpdate();
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Connection connection = dataSource.getConnection();
        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();

        Long calculateId = calculateCheck(connection, jobParameters.getString("today"), jobParameters.getString("day"));
        if (calculateId == null) {
            log.error("CreateCalculatePreTasklet - No calculate Data!");
            return null;
        }
        initMenu(connection);
        log.info("execute - initMenu END: " + menuIdToNameMap);

        Map<String, Integer> result = getUserPolicy(connection, jobParameters.getString("day"));
        log.info("execute - getUserPolicy END: " + result);
        checkOrders(connection, jobParameters.getString("date"), result);
        log.info("execute - checkOrders END: " + result);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> predict = getPredictFood(connection, result);
        log.info("execute - getPredictFood END: " + predict);
        savePredictData(connection, calculateId, result.values().stream().mapToInt(Integer::intValue).sum(), objectMapper.writeValueAsString(predict), objectMapper.writeValueAsString(result));

        connection.close();
        clear();
        return RepeatStatus.FINISHED;
    }

    private void clear() {
        menuNameToIdMap.clear();
        menuIdToNameMap.clear();
    }
}
