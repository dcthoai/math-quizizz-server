package math.server.repository.impl;

import math.server.common.Common;
import math.server.model.User;
import math.server.repository.IUserRepository;
import math.server.repository.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository implements IUserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final Connection connection;

    public UserRepository() {
        this.connection = new DatabaseConnection().getConnection();
    }

    @Override
    public User findUserById(int id) {
        String sql = "SELECT * FROM Users WHERE id = ?";

        try {
            PreparedStatement query = connection.prepareStatement(sql);
            query.setInt(1, id);

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                Common.objectMapper(resultSet, user);

                return user;
            }
        } catch (Exception e) {
            log.error("Failed to find user by ID: {}", id);
        }

        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";

        try {
            PreparedStatement query = connection.prepareStatement(sql);
            query.setString(1, username);

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                Common.objectMapper(resultSet, user);

                return user;
            }
        } catch (Exception e) {
            log.error("Failed to find user by username: {}", username);
        }

        return null;
    }
}
