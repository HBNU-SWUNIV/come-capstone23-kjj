package com.batch.batch.batch.order.task.predictweek;

import com.batch.batch.batch.order.task.predictweek.method.CreatePredictWeekMethod;
import com.batch.batch.tools.DateTools;
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
import java.util.*;

@Slf4j
@Component
public class CreatePredictWeekTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CreatePredictWeekMethod method;

    public CreatePredictWeekTasklet(@Qualifier("dataDataSource") DataSource dataSource, CreatePredictWeekMethod method) {
        this.dataSource = dataSource;
        this.method = method;
    }

    // 이번 주 월~금 이용인원 계산
    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        Map<String, List<Long>> doubleCheckMap = new HashMap<>();
        String[] day = DateTools.getDayArray();
        for (String d : day) {
            result.put(d, 0);
            doubleCheckMap.put(d, new ArrayList<>());
        }
        Connection connection = dataSource.getConnection();

        method.countOrders(connection, result, doubleCheckMap);
        method.checkPolicy(connection, result, doubleCheckMap);

        String insertQuery = "insert into calculate_pre_week(date, monday, tuesday, wednesday, thursday, friday) values(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, DateTools.getDate());
            insertStatement.setInt(2, result.get(day[0]));
            insertStatement.setInt(3, result.get(day[1]));
            insertStatement.setInt(4, result.get(day[2]));
            insertStatement.setInt(5, result.get(day[3]));
            insertStatement.setInt(6, result.get(day[4]));
            insertStatement.executeUpdate();
        }
        return RepeatStatus.FINISHED;
    }
}