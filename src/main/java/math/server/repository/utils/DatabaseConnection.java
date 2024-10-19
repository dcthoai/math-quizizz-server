package main.java.math.server.repository.utils;

import main.java.math.server.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseConnection {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private long lastUsedTime, connectionStartTime;
    private Connection connection;

    public DatabaseConnection() {}

    private boolean isIdleTimeout() {
        return (System.currentTimeMillis() - lastUsedTime) > Constants.CONNECTION_TIMEOUT;
    }

    private boolean isExpired() {
        return (System.currentTimeMillis() - connectionStartTime) > Constants.CONNECTION_LIFETIME;
    }

    public Connection connect() {
        try {
            // Registering the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing a connection to the database
            Connection connection = DriverManager.getConnection(Constants.DATABASE_HOST, Constants.DATABASE_USER, Constants.DATABASE_KEY);
            log.info("Connection established successfully.");

            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Could not found class or drive error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to connect to the database: " + e.getMessage());
        }

        return null;
    }

    public void closeConnection() {
        try {
            if (Objects.nonNull(connection) && !connection.isClosed()) {
                connection.close();
                log.info("Connection closed successfully.");
            } else {
                log.warn("Connection is null or not initialized");
            }
        } catch (SQLException e) {
            log.error("Failed to close the connection: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (Objects.isNull(connection) || connection.isClosed() || isIdleTimeout() || isExpired()) {
                closeConnection(); // Close old connection if expired or unused
                connection = connect();
                connectionStartTime = System.currentTimeMillis();
            }

            lastUsedTime = System.currentTimeMillis();  // Update connection usage time
        } catch (SQLException e) {
            log.error("Failed to check connection status: " + e.getMessage());
        }

        return connection;
    }
}
