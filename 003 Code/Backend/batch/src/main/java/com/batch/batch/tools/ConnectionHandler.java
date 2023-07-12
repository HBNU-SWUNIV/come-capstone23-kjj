package com.batch.batch.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class ConnectionHandler {
    private final SlackTools slackTools;
    public void execute(Connection connection, ExceptionRunnable codeBlock) throws Exception {
        try {
            connection.setAutoCommit(false);
            codeBlock.run();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            slackTools.sendSlackMessage(e, codeBlock.getClass().getName());
            throw e;
        } finally {
            connection.close();
        }
    }
}
