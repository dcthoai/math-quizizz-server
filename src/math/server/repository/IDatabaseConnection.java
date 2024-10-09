package math.server.repository;

import java.sql.Connection;

public interface IDatabaseConnection {

    Connection connect();

    void closeConnection(Connection connection);
}
