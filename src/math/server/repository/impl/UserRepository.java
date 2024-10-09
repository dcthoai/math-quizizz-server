package math.server.repository.impl;

import math.server.common.Common;
import math.server.model.User;
import math.server.repository.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends DatabaseConnection implements IUserRepository {

    private final Connection connection;

    public UserRepository() {
        this.connection = connect();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return null;
    }
}
