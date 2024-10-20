package main.java.math.server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import main.java.math.server.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {

    private static final Logger log = LoggerFactory.getLogger(DataSourceProvider.class);
    private static final DataSourceProvider instance = new DataSourceProvider();
    private static HikariDataSource dataSource;

    private DataSourceProvider() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Constants.DATABASE_HOST);
        config.setUsername(Constants.DATABASE_USER);
        config.setPassword(Constants.DATABASE_KEY);

        config.setMaximumPoolSize(10); // Maximum number of connections
        config.setMinimumIdle(2); // Minimum number of connections
        config.setIdleTimeout(10000); // Maximum time a connection is idle
        config.setMaxLifetime(600000); // Maximum time of a connection
        config.setConnectionTimeout(10000); // Time to wait for a connection

        dataSource = new HikariDataSource(config);
        log.info("Configuration data source for application successfully");
    }

    public static DataSourceProvider getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Failed to get connection: ", e);
            return null;
        }
    }
}
