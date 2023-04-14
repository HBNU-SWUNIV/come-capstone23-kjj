package com.batch.batch.batch.order.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class CountOrdersByDateTasklet implements Tasklet {

    private final DataSource dataSource;

    private String getTodayToString() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = getTodayToString();

        String getQuery = "select count(*) from orders where order_date = ? and recognize = 1";
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getQuery);
        statement.setNString(1, date);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int result = resultSet.getInt(1);

        String insertQuery = "insert into store_state(date, congestion, today) values(?, null, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setNString(1, date);
        insertStatement.setInt(2, result);
        insertStatement.executeUpdate();

        return RepeatStatus.FINISHED;
    }
}
