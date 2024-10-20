package main.java.math.server.service.impl;

import main.java.math.server.dto.request.UserRequest;
import main.java.math.server.model.User;
import main.java.math.server.repository.impl.UserRepository;
import main.java.math.server.service.IUserService;

import java.util.Objects;

public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
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
