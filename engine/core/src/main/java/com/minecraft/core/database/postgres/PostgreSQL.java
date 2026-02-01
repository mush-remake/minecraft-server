package com.minecraft.core.database.postgres;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQL {

    private final PostgreSQLProperties properties;
    private HikariDataSource dataSource;
    private Connection connection;

    public PostgreSQL(PostgreSQLProperties properties) {
        this.properties = properties;
    }

    public PostgreSQL connect() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            return this;
        }

        HikariConfig config = new HikariConfig();

        String url = "jdbc:postgresql://" + properties.getHost() + ":" + properties.getPort() + "/" + properties.getDatabase();

        config.setJdbcUrl(url);
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("core-postgresql-pool");
        config.setConnectionTimeout(10000L);
        config.setIdleTimeout(600000L);
        config.setMaxLifetime(1800000L);

        this.dataSource = new HikariDataSource(config);

        try {
            this.connection = this.dataSource.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    public int insertId(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new IllegalStateException("Nenhuma chave auto gerada foi encontrada");
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                int attempts = 0;
                long delay = 1000L;
                while (attempts < 3) {
                    try {
                        connection = dataSource.getConnection();
                        if (connection != null && connection.isValid(2)) break;
                    } catch (SQLException ignored) {
                    }
                    attempts++;
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ignored) {
                    }
                    delay *= 2;
                }
            }
        } catch (SQLException ignored) {
        }
        return connection;
    }

    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }

        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
