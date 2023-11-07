package com.batch.batch.batch.order.task.calculate;

import com.batch.batch.batch.order.task.calculate.method.CreateLeftoverPreMethod;
import lombok.extern.slf4j.Slf4j;
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
public class CreateLeftoverPreTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CreateLeftoverPreMethod method;

    public CreateLeftoverPreTasklet(@Qualifier("dataDataSource") DataSource dataSource, CreateLeftoverPreMethod method) {
        this.dataSource = dataSource;
        this.method = method;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Integer> calculateData = method.getCalculateData(connection);
            int id = calculateData.get("id");
            int today = calculateData.get("today");

            String query = "insert into leftover_pre(calculate_id, predict) values(?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                double aver = 120;
                statement.setDouble(2, aver * today);

                statement.executeUpdate();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
