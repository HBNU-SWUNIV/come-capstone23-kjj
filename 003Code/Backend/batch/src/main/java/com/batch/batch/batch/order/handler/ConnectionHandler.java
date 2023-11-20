package com.batch.batch.batch.order.handler;

import java.sql.Connection;

public interface ConnectionHandler {
    public void execute(Connection connection, ExceptionRunnable runnable) throws Exception;
}
