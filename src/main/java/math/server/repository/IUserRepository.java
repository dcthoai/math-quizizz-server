package math.server.repository;

import math.server.entity.User;

public interface IUserRepository {

    User findUserByUsername(String username);
}
