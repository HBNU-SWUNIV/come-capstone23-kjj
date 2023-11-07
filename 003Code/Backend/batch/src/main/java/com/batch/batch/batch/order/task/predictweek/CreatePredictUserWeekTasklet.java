package com.batch.batch.batch.order.task.predictweek;

import com.batch.batch.batch.order.task.predictweek.method.CreatePredictUserWeekMethod;
import com.batch.batch.object.FoodPredict;
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
import java.util.Map;

@Slf4j
@Component
public class CreatePredictUserWeekTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CreatePredictUserWeekMethod method;

    public CreatePredictUserWeekTasklet(@Qualifier("dataDataSource") DataSource dataSource, CreatePredictUserWeekMethod method) {
        this.dataSource = dataSource;
        this.method = method;
    }

    // 매출액 기반 월~금 이용자 수, 전체 인원 중 이용자 비율 기반 월~금 이용자 수
    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            long entireUser = 3000;

            Map<Long, Integer> menuFood = method.getEntireMenuFood(connection);
            FoodPredict entire = method.getEntire(connection, entireUser, menuFood);
            FoodPredict part = method.getPart(connection, entireUser, menuFood);

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
        }
        return RepeatStatus.FINISHED;
    }
}