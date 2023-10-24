package com.batch.batch.batch.order.task.predictweek;

import com.batch.batch.object.FoodPredict;
import com.batch.batch.tools.DateTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CreatePredictUserWeekTasklet implements Tasklet {

    private final DataSource dataSource;

    private final String[] day = {"monday", "tuesday", "wednesday", "thursday", "friday"};

    public CreatePredictUserWeekTasklet(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 매출액 기반 월~금 이용자 수, 전체 인원 중 이용자 비율 기반 월~금 이용자 수
    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Connection connection = dataSource.getConnection();

        Map<Long, Integer> menuFood = getEntireMenuFood(connection);
        FoodPredict entire = getEntire(connection, getMenuCostAvg(connection), menuFood);
        FoodPredict part = getPart(connection, getRatio(connection), menuFood);

        String insertQuery = "insert into weekly_food_predict(date, entire_monday, entire_tuesday, entire_wednesday, entire_thursday, entire_friday, monday, tuesday, wednesday, thursday, friday) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, DateTools.getDate());
            insertStatement.setLong(2, Math.round(entire.getMonday()));
            insertStatement.setLong(3, Math.round(entire.getTuesday()));
            insertStatement.setLong(4, Math.round(entire.getWednesday()));
            insertStatement.setLong(5, Math.round(entire.getThursday()));
            insertStatement.setLong(6, Math.round(entire.getFriday()));
            insertStatement.setLong(7, Math.round(part.getMonday()));
            insertStatement.setLong(8, Math.round(part.getTuesday()));
            insertStatement.setLong(9, Math.round(part.getWednesday()));
            insertStatement.setLong(10, Math.round(part.getThursday()));
            insertStatement.setLong(11, Math.round(part.getFriday()));
            insertStatement.executeUpdate();
        }
        return RepeatStatus.FINISHED;
    }

    private Integer getMenuCostAvg(Connection connection) throws SQLException {
        String query = "select avg(cost) as average from menu;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("average");
            }
        }
    }

    private int countMenu(Connection connection) throws SQLException {
        String query = "select count(*) as count from menu;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count");
            }
        }
    }

    private double getEntireData(Map<Long, Integer> foodMap, double data, int unit, int avg, int count) {
        // 요일 별 메뉴당 인원 계산
        long v = Math.round((data * unit / avg) / count);
        return foodMap.values().stream().mapToLong(value -> value * v).sum();
    }

    private FoodPredict getEntire(Connection connection, Integer avg, Map<Long, Integer> menuFood) throws SQLException {
        int unit = 10000;
        int count = countMenu(connection);
        String query = "select * from revenue order by id desc limit 1;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new FoodPredict(
                        getEntireData(menuFood, resultSet.getDouble(day[0]), unit, avg, count),
                        getEntireData(menuFood, resultSet.getDouble(day[1]), unit, avg, count),
                        getEntireData(menuFood, resultSet.getDouble(day[2]), unit, avg, count),
                        getEntireData(menuFood, resultSet.getDouble(day[3]), unit, avg, count),
                        getEntireData(menuFood, resultSet.getDouble(day[4]), unit, avg, count)
                );
            }
        }
    }

    private double getRatio(Connection connection) throws SQLException {
        String query = "select count(*) as result from user;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                double entireUser = 2000.0;
                return resultSet.getDouble("result") / entireUser * 100.0;
            }
        }
    }

    private void checkSelfOrders(Connection connection, Map<String, Integer> dayFoodMap, String d, String date, Map<Long, Integer> menuFood) throws SQLException {
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

    private FoodPredict getPart(Connection connection, double ratio, Map<Long, Integer> menuFood) throws SQLException {
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
                log.info("isBefore IN - day = {}, now = {}", thisMonday, LocalDate.now());
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
    private Map<Long, Integer> getEntireMenuFood(Connection connection) throws SQLException, JsonProcessingException {
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