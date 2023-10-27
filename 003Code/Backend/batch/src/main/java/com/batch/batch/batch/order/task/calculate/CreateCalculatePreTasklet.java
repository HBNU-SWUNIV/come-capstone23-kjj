package com.batch.batch.batch.order.task.calculate;

import com.batch.batch.batch.order.task.calculate.method.CreateCalculatePreMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobParameters;
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
import java.util.*;

@Slf4j
@Component
public class CreateCalculatePreTasklet implements Tasklet {

    private final DataSource dataSource;
    private final CreateCalculatePreMethod method;
    private static final Map<Long, String> menuIdToNameMap = new HashMap<>();
    private static final Map<String, Long> menuNameToIdMap = new HashMap<>();
    private static final Map<String, Long> menuNameToFoodIdMap = new HashMap<>();

    public CreateCalculatePreTasklet(@Qualifier("dataDataSource") DataSource dataSource, CreateCalculatePreMethod method) {
        this.dataSource = dataSource;
        this.method = method;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        Connection connection = dataSource.getConnection();
        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();

        Long calculateId = method.calculateCheck(connection, jobParameters.getString("today"), jobParameters.getString("day"));
        if (calculateId == null) {
            log.error("CreateCalculatePreTasklet - No calculate Data!");
            return null;
        }
        method.initMenu(connection, menuIdToNameMap, menuNameToIdMap, menuNameToFoodIdMap);
        log.info("execute - initMenu END: " + menuIdToNameMap);

        Map<String, Integer> result = method.getUserPolicy(connection, jobParameters.getString("day"), menuIdToNameMap);
        log.info("execute - getUserPolicy END: " + result);
        method.checkOrders(connection, jobParameters.getString("date"), result);
        log.info("execute - checkOrders END: " + result);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> predict = method.getPredictFood(connection, result, menuNameToFoodIdMap);
        log.info("execute - getPredictFood END: " + predict);
        method.savePredictData(connection, calculateId, result.values().stream().mapToInt(Integer::intValue).sum(), objectMapper.writeValueAsString(predict), objectMapper.writeValueAsString(result));

        connection.close();
        clear();
        return RepeatStatus.FINISHED;
    }

    private void clear() {
        menuNameToIdMap.clear();
        menuIdToNameMap.clear();
        menuNameToFoodIdMap.clear();
    }
}
