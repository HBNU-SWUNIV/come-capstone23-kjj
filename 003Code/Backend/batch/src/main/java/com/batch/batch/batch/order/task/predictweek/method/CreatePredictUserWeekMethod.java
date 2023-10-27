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
    public Integer getMenuCostAvg(Connection connection) throws SQLException {
        String query = "select avg(cost) as average from menu;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("average");
            }
        }
    }

    public int countMenu(Connection connection) throws SQLException {
        String query = "select count(*) as count from menu;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count");
            }
        }
    }

    public int getEntireData(Map<Long, Integer> foodMap, double revenue, int unit, int avg, int count, long entireUser) {
        int people = (int) (revenue * unit / avg) / count;
        int a1 = people * count;
        int foodResult = foodMap.values().stream().mapToInt(value -> value * people).sum();

        return (int) ((foodResult * entireUser) / a1);
    }

    public FoodPredict getEntire(Connection connection, long entireUser, Integer avg, Map<Long, Integer> menuFood) throws SQLException {
        int unit = 10000;
        int count = countMenu(connection);
        String query = "select * from revenue order by id desc limit 1;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new FoodPredict(
                        getEntireData(menuFood, resultSet.getDouble(day[0]), unit, avg, count, entireUser),
                        getEntireData(menuFood, resultSet.getDouble(day[1]), unit, avg, count, entireUser),
                        getEntireData(menuFood, resultSet.getDouble(day[2]), unit, avg, count, entireUser),
                        getEntireData(menuFood, resultSet.getDouble(day[3]), unit, avg, count, entireUser),
                        getEntireData(menuFood, resultSet.getDouble(day[4]), unit, avg, count, entireUser)
                );
            }
        }
    }

    public double getRatio(Connection connection, long entireUser) throws SQLException {
        String query = "select count(*) as result from user where roles='ROLE_USER';";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getDouble("result") / entireUser * 100.0;
            }
        }
    }

    public void checkSelfOrders(Connection connection, Map<String, Integer> dayFoodMap, String d, String date, Map<Long, Integer> menuFood) throws SQLException {
        String query = "select count(orders.id) as count, menu.id from orders join menu on order_date= ? and orders.menu = menu.name  group by menu.id;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long menuId = resultSet.getLong("id");
                    if (!menuFood.containsKey(menuId)) {
                        log.warn("TRASH DATA FOUND : menuId = {}", menuId);
                        continue;
                    }
                    dayFoodMap.put(d, dayFoodMap.get(d) - (menuFood.get(menuId) * resultSet.getInt("count")));
                }
            }
        }
    }

    public FoodPredict getPart(Connection connection, double ratio, Map<Long, Integer> menuFood) throws SQLException {
        Map<String, Integer> dayFoodMap = new HashMap<>();
        LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (String d : day) {
            int dayFood = 0;
            String query = "select count(*) as count, default_menu from user_policy where " + d + " = 1 and default_menu group by default_menu;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long menuId = resultSet.getLong("default_menu");
                        int count = resultSet.getInt("count");
                        if (!menuFood.containsKey(menuId)) {
                            log.warn("TRASH DATA FOUND : menuId = {}", menuId);
                            continue;
                        }
                        dayFood += menuFood.get(menuId) * count;
                    }
                }
            }
            dayFoodMap.put(d, dayFood);
            if (DateTools.isNotBefore(thisMonday)) {
                checkSelfOrders(connection, dayFoodMap, d, thisMonday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), menuFood);
            }
            thisMonday = thisMonday.plusDays(1);
        }
        return new FoodPredict(
                dayFoodMap.get(day[0]) / ratio * 100,
                dayFoodMap.get(day[1]) / ratio * 100,
                dayFoodMap.get(day[2]) / ratio * 100,
                dayFoodMap.get(day[3]) / ratio * 100,
                dayFoodMap.get(day[4]) / ratio * 100
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