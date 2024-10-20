package math.server.service.impl;

import math.server.dto.request.UserRequest;
import math.server.model.User;
import math.server.repository.impl.UserRepository;
import math.server.service.IUserService;

import java.util.Objects;

public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    @Override
    public Integer save(UserRequest user) {
        return userRepository.insert("user", new User(user.getUsername(), user.getPassword()));
    }

    @Override
    public User findUserById(int id) {
        return (User) userRepository.findOne(id, User.class);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean checkLogin(UserRequest user) {
        User account = null;

        if (Objects.nonNull(user.getUsername())) {
            account = userRepository.findUserByUsername(user.getUsername());
        }

        return Objects.nonNull(account) && Objects.equals(user.getPassword(), account.getPassword());
    }
}
