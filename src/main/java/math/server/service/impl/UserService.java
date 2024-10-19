package main.java.math.server.service.impl;

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
        return userRepository.findUserById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean checkLogin(User user) {
        User account = null;

        if (Objects.nonNull(user.getUsername())) {
            account = userRepository.findUserByUsername(user.getUsername());
        } else if (Objects.nonNull(user.getId())) {
            account = userRepository.findUserById(user.getId());
        }

        if (Objects.nonNull(account)) {
            if (Objects.equals(user.getPassword(), account.getPassword())) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
