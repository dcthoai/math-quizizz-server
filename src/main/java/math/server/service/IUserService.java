package math.server.service;

import math.server.dto.request.UserRequest;
import math.server.entity.User;

public interface IUserService {

    Integer save(UserRequest user);

    User findUserById(int id);

    User findUserByUsername(String username);

    User checkLogin(UserRequest user);
}
