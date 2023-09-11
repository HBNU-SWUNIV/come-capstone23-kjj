package com.batch.batch.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class ConnectionHandler {
    private final SlackTools slackTools;
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
