package com.batch.batch.batch.order.tasklet;

import com.batch.batch.pojo.UserPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class CreateTodayOrderTasklet {

    @Bean
    @StepScope
    public JdbcCursorItemReader<UserPolicy> todayUserReader(@Value("#{jobParameters['today']}") String today) {
        String query = "select * from user_policy where ? = 1";
        return new JdbcCursorItemReaderBuilder<UserPolicy>()
                .name("jpaCursorItemReader")
                .sql(query)
                .preparedStatementSetter(new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setNString(1, today);
                    }
                })
                .build();
    }
}