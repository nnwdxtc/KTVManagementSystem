// src/main/java/com/ktv/util/DBUtil.java
package com.ktv.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DBUtil.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            props.load(input);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("db.driver"));
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setMaximumPoolSize(
                    Integer.parseInt(props.getProperty("db.maximumPoolSize")));
            config.setMinimumIdle(
                    Integer.parseInt(props.getProperty("db.minimumIdle")));
            config.setConnectionTimeout(
                    Long.parseLong(props.getProperty("db.connectionTimeout")));
            config.setIdleTimeout(
                    Long.parseLong(props.getProperty("db.idleTimeout")));
            config.setMaxLifetime(
                    Long.parseLong(props.getProperty("db.maxLifetime")));

            dataSource = new HikariDataSource(config);
            logger.info("数据库连接池初始化成功");
        } catch (Exception e) {
            logger.error("数据库连接池初始化失败", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("关闭数据库连接失败", e);
            }
        }
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}