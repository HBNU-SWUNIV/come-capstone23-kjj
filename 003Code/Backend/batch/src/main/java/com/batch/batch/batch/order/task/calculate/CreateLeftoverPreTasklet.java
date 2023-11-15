package com.batch.batch.batch.order.task.calculate;

import com.batch.batch.batch.order.task.calculate.method.CreateLeftoverPreMethod;
import com.batch.batch.tools.DateTools;
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
            Integer today = method.getCalculateData(connection);

            String query = "insert into leftover_pre(date, predict) values(?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, DateTools.getDate());
                double aver = 120;
                statement.setDouble(2, aver * today);

                statement.executeUpdate();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
