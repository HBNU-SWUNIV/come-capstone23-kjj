package com.batch.batch.batch.order.task.order;

import com.batch.batch.object.Order;
import com.batch.batch.object.UserPolicy;
import com.batch.batch.tools.DateTools;
import lombok.Getter;
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
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CreateTodayOrder {

    private final DataSource dataSource;

    private static final Map<Long, String> idToNameMap = new HashMap<>();
    @Getter
    private static final Map<String, Integer> nameToCostMap = new HashMap<>();

    public CreateTodayOrder(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void clearNameToCostMap() {
        nameToCostMap.clear();
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
        String query = "select default_menu, id from user_policy where " + today + " = 1 and default_menu";
        return new JdbcCursorItemReaderBuilder<UserPolicy>()
                .name("jdbcCursorItemReader")
                .sql(query)
                .dataSource(dataSource)
                .rowMapper((rs, rowNum) -> new UserPolicy(
                        rs.getString("default_menu"),
                        rs.getString("id")
                ))
                .build();
    }

    @Bean
    public ItemProcessor<UserPolicy, Order> createOrderProcessor() {

        return item -> {
            boolean exists;
            Long userId = item.getId();
            Long defaultMenu = item.getDefaultMenu();
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
                    } catch (SQLIntegrityConstraintViolationException e) {
                        log.warn("TRASH DATA FOUND : userId = {}", userId);
                        return null;
                    }
                    return new Order(userId, cost, defaultMenu, date, true);
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