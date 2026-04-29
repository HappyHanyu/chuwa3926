package com.example.training_project.hw11.Q12;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleConnectionPool {
    private final BlockingQueue<Connection> available;
    private final Set<Connection> inUse;
    private final int maxSize;
    private final DataSource dataSource;

    public SimpleConnectionPool(DataSource ds, int minSize, int maxSize) throws SQLException {
        if (ds == null) {
            throw new IllegalArgumentException("DataSource cannot be null");
        }
        if (minSize < 0 || maxSize <= 0 || minSize > maxSize) {
            throw new IllegalArgumentException("Invalid pool size");
        }

        this.dataSource = ds;
        this.maxSize = maxSize;
        this.available = new LinkedBlockingQueue<>();
        this.inUse = ConcurrentHashMap.newKeySet();

        for (int i = 0; i < minSize; i++) {
            available.offer(dataSource.getConnection());
        }
    }

    public synchronized Connection getConnection(long timeoutMs) throws SQLException {
        Connection conn = available.poll();

        if (conn == null) {
            if (totalConnections() < maxSize) {
                conn = dataSource.getConnection();
            } else {
                try {
                    conn = available.poll(timeoutMs, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Interrupted while waiting for connection", e);
                }

                if (conn == null) {
                    throw new SQLException("Timeout waiting for connection");
                }
            }
        }

        inUse.add(conn);
        return conn;
    }

    public synchronized void releaseConnection(Connection conn) {
        if (conn == null) {
            return;
        }

        if (inUse.remove(conn)) {
            available.offer(conn);
        }
    }

    private int totalConnections() {
        return available.size() + inUse.size();
    }
}
