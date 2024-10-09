package math.server.repository;

import math.server.model.User;

public interface IUserRepository {

    User findUserById(int id);
    User findUserByUsername(String username);
}
