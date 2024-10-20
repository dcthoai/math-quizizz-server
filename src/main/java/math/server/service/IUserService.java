package main.java.math.server.service;

import main.java.math.server.dto.request.UserRequest;
import main.java.math.server.model.User;

public interface IUserService {

    User findUserById(int id);

    User findUserByUsername(String username);

    boolean checkLogin(UserRequest user);
}
