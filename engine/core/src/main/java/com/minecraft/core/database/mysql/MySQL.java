/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package com.minecraft.core.database.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    private final MySQLProperties properties;
    private HikariDataSource dataSource;
    private Connection connection;

    public MySQL(MySQLProperties properties) {
        this.properties = properties;
    }

    public MySQL connect() {
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
        return connection;
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
