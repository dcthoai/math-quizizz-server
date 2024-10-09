package math.server.repository.impl;

import math.server.repository.IDatabaseConnection;
import math.server.common.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements IDatabaseConnection {

    @Override
    public Connection connect() {
        try {
            // Registering the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing a connection to the database
            Connection connection = DriverManager.getConnection(Constants.DATABASE_HOST, Constants.DATABASE_USER, Constants.DATABASE_KEY);
            System.out.println("Connection established successfully.");

            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to close the connection.");
            }
        }
    }
}
