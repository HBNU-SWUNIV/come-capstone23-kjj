package com.batch.batch.batch.order.task.calculate.method;

import com.batch.batch.batch.order.task.start.method.StartTodayBatchMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCalculatePreMethod {
    private final StartTodayBatchMethod batchMethod;
    public Long calculateCheck(Connection connection, String today, String day) throws SQLException {
        // 만약 일요일이여서 월요일 calculate 데이터가 없다면 생성
        if (day.equals("monday")) {
            Long batchDateId = batchMethod.getTodayBatchDateId(connection);
            String query = "insert into calculate(batch_date_id, today, sales) values(?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, batchDateId);
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                statement.executeUpdate();
            }
        }
        // calculate id 조회
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

    // Map 초기화
    public void initMenu(Connection connection, Map<Long, String> menuIdToNameMap, Map<String, Long> menuNameToIdMap, Map<String, Long> menuNameToFoodIdMap) throws SQLException {
        String query = "select id, menu_food_id, name from menu";
        try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long foodId = resultSet.getLong("menu_food_id");
                String name = resultSet.getString("name");
                menuIdToNameMap.put(id, name);
                menuNameToIdMap.put(name, id);
                menuNameToFoodIdMap.put(name, foodId);
            }
        }
    }

    // 유저 정책으로 익일 사용자 수 계산
    public Map<String, Integer> getUserPolicy(Connection connection, String day, Map<Long, String> menuIdToNameMap) throws SQLException {
        Map<String, Integer> result = new HashMap<>();
        String query = "select default_menu, COUNT(*) as count from user_policy where " + day + " = 1 and default_menu group by default_menu";

        try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
            while (resultSet.next()) {
                String defaultMenu = menuIdToNameMap.get(resultSet.getLong("default_menu"));
                int count = resultSet.getInt("count");
                result.put(defaultMenu, count);
            }
        }
        // result는 defaultMenu 이름, count
        return result;
    }

    // 정책 이외에 수동 설정된 예약 내역 조회
    public void checkOrders(Connection connection, String date, Map<String, Integer> result) throws SQLException {
        String query = "select id, menu, recognize from orders where order_date = ?";

        List<Long> updateRecognizeIds = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String menu = resultSet.getString("menu");
                    int type = resultSet.getInt("recognize");
                    // 만약 수동 설정한 이후 해당 메뉴가 삭제되었을 경우 업데이트
                    if (!result.containsKey(menu)) updateRecognizeIds.add(id);
                    else {
                        if (type == 1) result.put(menu, result.get(menu) + 1);
                        else result.put(menu, result.get(menu) - 1);
                    }
                }
            }
        }
        updateOrderRecognize(connection, updateRecognizeIds);
    }

    public void updateOrderRecognize(Connection connection, List<Long> ids) throws SQLException {
        for (Long id : ids) {
            String query = "update orders set recognize = 0 where id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        }
    }

    // 식재료 계산
    // map = result
    public Map<String, Integer> getPredictFood(Connection connection, Map<String, Integer> map, Map<String, Long> menuNameToFoodIdMap) throws SQLException, JsonProcessingException {
        Map<String, Integer> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<String, Integer> data : map.entrySet()) {
            String menuName = data.getKey();
            Long foodId = menuNameToFoodIdMap.get(menuName);
            if (foodId == null) continue;
            String query = "select food from menu_food where id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, foodId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String menu = resultSet.getString("food");
                        Map<String, Integer> foods = objectMapper.readValue(menu, Map.class);
                        for (Map.Entry<String, Integer> food : foods.entrySet()) {
                            String key = food.getKey();
                            Integer value = food.getValue();
                            if (result.containsKey(key)) result.put(key, result.get(key) + value * map.get(menuName));
                            else result.put(key, value * map.get(menuName));
                        }
                    }
                }
            }
        }
        return result;
    }

    public void savePredictData(Connection connection, Long id, int predictUser, String predictFood, String predictMenu) throws SQLException {
        String query = "insert into calculate_pre(calculate_id, predict_user, predict_food, predict_menu) values(?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.setInt(2, predictUser);
            statement.setString(3, predictFood);
            statement.setString(4, predictMenu);
            statement.executeUpdate();
        }
    }
}
