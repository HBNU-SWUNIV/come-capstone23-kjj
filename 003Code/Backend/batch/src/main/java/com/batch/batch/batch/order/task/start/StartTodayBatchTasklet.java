package com.batch.batch.batch.order.task.start;

import com.batch.batch.tool.DateTool;
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

@Slf4j
@Component
public class StartTodayBatchTasklet implements Tasklet {
    private final DataSource dataSource;

    public StartTodayBatchTasklet(@Qualifier("dataDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String insertQuery = "insert into batch_date(date) values(?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, DateTool.getDate());
                insertStatement.executeUpdate();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
