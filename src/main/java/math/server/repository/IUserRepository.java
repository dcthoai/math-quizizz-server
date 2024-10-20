package main.java.math.server.repository;

import main.java.math.server.model.User;

public interface IUserRepository {

    User findUserByUsername(String username);
}
