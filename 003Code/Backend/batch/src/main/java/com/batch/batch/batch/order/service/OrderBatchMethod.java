package com.batch.batch.batch.order.service;

import com.batch.batch.tools.DateTools;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderBatchMethod {

    private final DataSource dataSource;

    public OrderBatchMethod(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isOffDay(String date) throws SQLException {
        String offQuery = "select exists(select * from store_state where date = ? and off = true)";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(offQuery)) {
                statement.setString(1, date);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) return resultSet.getBoolean(1);
                }
            }
        }
        return false;
    }

    public void clear() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Long id = getCalculateId(connection);
            if (id == null) return;

            sendDelQuery(connection, id);
        }
    }

    private Long getCalculateId(Connection connection) throws SQLException{
        String getQuery = "select id from calculate where date = ?;";
        try (PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, DateTools.getDate());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong("id");
            } catch (SQLException e) {
                return null;
            }
        }
    }

    private void sendDelQuery(Connection connection, long id) throws SQLException{
        String delCalculatePreQuery = "delete from calculate_pre where calculate_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delCalculatePreQuery)) {
            statement.setLong(1, id);
            statement.execute();
        }

        String delCalculateMenuQuery = "delete from calculate_menu where calculate_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delCalculateMenuQuery)) {
            statement.setLong(1, id);
            statement.execute();
        }

        String delCalculateQuery = "delete from calculate where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delCalculateQuery)) {
            statement.setLong(1, id);
            statement.execute();
        }
    }
}
