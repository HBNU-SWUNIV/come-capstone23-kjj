package com.batch.batch.batch.order.aop.handler;

import com.batch.batch.tools.SlackTools;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class ConnectionHandlerV1 implements ConnectionHandler {
    private final SlackTools slackTools;

    @Override
    public void execute(Connection connection, ExceptionRunnable runnable) throws Exception {
        try {
            connection.setAutoCommit(false);
            runnable.run();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            slackTools.sendSlackErrorMessage(e, runnable.getClass().getName());
            throw e;
        } finally {
            connection.close();
        }
    }
}