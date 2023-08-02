package com.batch.batch.batch.order.task;

import com.batch.batch.pojo.Order;
import com.batch.batch.pojo.UserPolicy;
import com.batch.batch.tools.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
public class CreateTodayOrder {

    private final DataSource dataSource;

    private static Map<Long, String> idToNameMap = new HashMap<>();
    private static Map<String, Integer> nameToCostMap = new HashMap<>();

    public CreateTodayOrder(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Map<String, Integer> getNameToCostMap() {
        return nameToCostMap;
    }

    private void initMenu() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "select id, name, cost from menu";

            try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Integer cost = resultSet.getInt("cost");
                    idToNameMap.put(id, name);
                    nameToCostMap.put(name, cost);
                }
            }
        }
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<UserPolicy> todayUserReader(@Value("#{jobParameters['today']}") String today) throws SQLException {
        initMenu();
        String query = "select default_menu, user_id from user_policy where " + today + " = 1 and default_menu";
        return new JdbcCursorItemReaderBuilder<UserPolicy>()
                .name("jdbcCursorItemReader")
                .sql(query)
                .dataSource(dataSource)
                .rowMapper((rs, rowNum) -> new UserPolicy(
                        rs.getString("default_menu"),
                        rs.getString("user_id")
                ))
                .build();
    }

    @Bean
    public ItemProcessor<UserPolicy, Order> createOrderProcessor() {

        return item -> {
            boolean exists;
            Long userId = item.getUser_id();
            Long defaultMenu = item.getDefault_menu();
            String date = DateTools.getDate();

            try (Connection connection = dataSource.getConnection()) {
                String findOrderQuery = "select * from orders where user_id = ? and order_date = ?";
                try (PreparedStatement statement = connection.prepareStatement(findOrderQuery)) {
                    statement.setLong(1, userId);
                    statement.setString(2, date);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        exists = resultSet.next();
                    }
                }

                if (!exists && idToNameMap.containsKey(defaultMenu) && nameToCostMap.containsKey(idToNameMap.get(defaultMenu))) {
                    String menuName = idToNameMap.get(defaultMenu);
                    int cost = nameToCostMap.get(menuName);

                    String insertOrderQuery = "insert into orders (user_id, menu, cost, order_date, recognize) values (?, ?, ?, ?, ?)";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertOrderQuery);) {
                        insertStatement.setLong(1, userId);
                        insertStatement.setString(2, menuName);
                        insertStatement.setInt(3, cost);
                        insertStatement.setString(4, date);
                        insertStatement.setBoolean(5, true);

                        insertStatement.executeUpdate();
                    }
                    throw new IllegalAccessException("dd");
                    //return new Order(userId, cost, defaultMenu, date, true);
                }
            }
            return null;
        };
    }

    @Bean
    public ItemWriter<Order> orderItemWriter() {
        return chunk -> log.info(DateTools.getDate() + "(" + DateTools.getToday() + ") 오더 생성 완료 - 신규 Order : " + chunk.size());
    }
}