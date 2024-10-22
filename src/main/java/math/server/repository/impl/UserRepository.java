package math.server.repository.impl;

import math.server.common.Common;
import math.server.model.User;
import math.server.repository.IUserRepository;
import math.server.repository.utils.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.List;

public class UserRepository extends BaseRepository implements IUserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM User WHERE username = ?";

        try {
            ResultSet resultSet = query(sql, List.of(username));

            if (resultSet.next()) {
                User user = new User();
                Common.objectMapper(resultSet, user);
//                resultSet.close();

                return user;
            }
        } catch (Exception e) {
            log.error("Failed to find user by username: {}", username, e);
        }

        return null;
    }
}
