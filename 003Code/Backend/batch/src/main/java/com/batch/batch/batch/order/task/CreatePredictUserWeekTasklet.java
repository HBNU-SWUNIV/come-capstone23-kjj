package com.batch.batch.batch.order.task;

import com.batch.batch.object.FoodPredict;
import com.batch.batch.tools.ConnectionHandler;
import com.batch.batch.tools.DateTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CreatePredictUserWeekTasklet implements Tasklet {

    private final DataSource dataSource;
    private final ConnectionHandler connectionHandler;

    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Connection connection = dataSource.getConnection();

        connectionHandler.execute(connection, () -> {
            FoodPredict entire = getEntire(connection, getMenuCostAvg(connection));
            double ratio = getRatio(connection);
            FoodPredict part = getPart(connection, ratio);

            String insertQuery = "insert into weekly_food_predict(date, entire_monday, entire_tuesday, entire_wednesday, entire_thursday, entire_friday, monday, tuesday, wednesday, thursday, friday) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setTimestamp(1, entire.getDate());
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
        });
        return RepeatStatus.FINISHED;
    }

    private Integer getMenuCostAvg(Connection connection) throws SQLException{
        String query = "select avg(cost) as average from menu;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("average");
            }
        }
    }

    private FoodPredict getEntire(Connection connection, Integer avg) throws SQLException {
        int unit = 10000;
        String query = "select * from revenue order by id desc limit 1;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new FoodPredict(
                        resultSet.getTimestamp("date"),
                        resultSet.getDouble("monday") * unit / avg,
                        resultSet.getDouble("tuesday") * unit / avg,
                        resultSet.getDouble("wednesday") * unit / avg,
                        resultSet.getDouble("thursday") * unit / avg,
                        resultSet.getDouble("friday") * unit / avg
                );
            }
        }
    }

    private double getRatio(Connection connection) throws SQLException{
        String query = "select count(*) as result from user;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                double entireUser = 2000.0;
                return resultSet.getDouble("result") / entireUser * 100.0;
            }
        }
    }

    private FoodPredict getPart(Connection connection, double ratio) throws SQLException {
        String query = "select * from calculate_pre_week order by id desc limit 1;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new FoodPredict(
                        null,
                        resultSet.getDouble("monday") / ratio * 100.0,
                        resultSet.getDouble("tuesday") / ratio * 100.0,
                        resultSet.getDouble("wednesday") / ratio * 100.0,
                        resultSet.getDouble("thursday") / ratio * 100.0,
                        resultSet.getDouble("friday") / ratio * 100.0
                );
            }
        }
    }
}