package math.server.service;

import math.server.dto.request.UserRequest;
import math.server.model.User;

public interface IUserService {

    Integer save(UserRequest user);

    User findUserById(int id);

    User findUserByUsername(String username);

    boolean checkLogin(UserRequest user);
}
