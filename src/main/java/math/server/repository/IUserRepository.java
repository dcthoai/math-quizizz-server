package math.server.repository;

import math.server.model.User;

public interface IUserRepository {

    User findUserByUsername(String username);
}
