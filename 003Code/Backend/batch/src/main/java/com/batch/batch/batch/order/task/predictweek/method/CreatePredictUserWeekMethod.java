package com.batch.batch.batch.order.task.predictweek.method;

import com.batch.batch.object.FoodPredict;
import com.batch.batch.tools.DateTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CreatePredictUserWeekMethod {

    private final String[] day = {"monday", "tuesday", "wednesday", "thursday", "friday"};

    private int countMenu(Connection connection) throws SQLException {
        String query = "select count(*) as count from menu;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count");
            }
        }
    }

    public int getEntireData(Map<Long, Integer> foodMap, int usersByMenu) {
        return foodMap.values().stream().mapToInt(value -> value * usersByMenu).sum();
    }

    public FoodPredict getEntire(Connection connection, long entireUser, Map<Long, Integer> menuFood) throws SQLException {
        int entireData = getEntireData(menuFood, (int) (entireUser / countMenu(connection)));
        return FoodPredict.of(entireData);
    }

    public void checkSelfOrders(Connection connection, Map<String, Integer> dayFoodMap, Map<String, Integer> userCountMap, String d, String date, Map<Long, Integer> menuFood) throws SQLException {
        String query = "select count(orders.id) as count, menu.id from orders join menu on order_date= ? and recognize and orders.menu = menu.name  group by menu.id;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long menuId = resultSet.getLong("id");
                    if (!menuFood.containsKey(menuId)) {
                        log.warn("TRASH DATA FOUND : menuId = {}", menuId);
                        continue;
                    }
                    int count = resultSet.getInt("count");
                    dayFoodMap.put(d, dayFoodMap.get(d) - (menuFood.get(menuId) * count));
                    userCountMap.put(d, userCountMap.get(d) - count);
                }
            }
        }
    }

    public FoodPredict getPart(Connection connection, long entireUser, Map<Long, Integer> menuFood) throws SQLException {
        Map<String, Integer> dayFoodMap = new HashMap<>();
        Map<String, Integer> userCountMap = new HashMap<>();
        LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (String d : day) {
            int dayFood = 0;
            int allUser = 0;
            String query = "select count(*) as count, default_menu from user_policy where " + d + " = 1 and default_menu group by default_menu;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long menuId = resultSet.getLong("default_menu");
                        int count = resultSet.getInt("count");
                        allUser += count;
                        if (!menuFood.containsKey(menuId)) {
                            log.warn("TRASH DATA FOUND : menuId = {}", menuId);
                            continue;
                        }
                        dayFood += menuFood.get(menuId) * count;
                    }
                }
            }
            dayFoodMap.put(d, dayFood);
            userCountMap.put(d, allUser);
            if (DateTools.isNotBefore(thisMonday)) {
                checkSelfOrders(connection, dayFoodMap, userCountMap, d, thisMonday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), menuFood);
            }
            thisMonday = thisMonday.plusDays(1);
        }
        return new FoodPredict(
                (double) (entireUser * dayFoodMap.get(day[0])) / userCountMap.get(day[0]),
                (double) (entireUser * dayFoodMap.get(day[1])) / userCountMap.get(day[1]),
                (double) (entireUser * dayFoodMap.get(day[2])) / userCountMap.get(day[2]),
                (double) (entireUser * dayFoodMap.get(day[3])) / userCountMap.get(day[3]),
                (double) (entireUser * dayFoodMap.get(day[4])) / userCountMap.get(day[4])
        );
    }

    // returnValue Key : menuId, Value : 총 식재료 양
    public Map<Long, Integer> getEntireMenuFood(Connection connection) throws SQLException, JsonProcessingException {
        Map<Long, Integer> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String query = "select menu.id, food from menu join menu_food on menu.menu_food_id = menu_food.id;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String food = resultSet.getString("food");
                    Map<String, Integer> foodMap = objectMapper.readValue(food, Map.class);
                    result.put(resultSet.getLong("id"), foodMap.values().stream().mapToInt(Integer::intValue).sum());
                }
            }
        }
        return result;
    }
}