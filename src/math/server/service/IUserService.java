package math.server.service;

import math.server.model.User;

public interface IUserService {

    User findUserById(int id);

    User findUserByUsername(String username);

    boolean checkLogin(User user);
}
