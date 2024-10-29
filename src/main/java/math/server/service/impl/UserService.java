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
        return userRepository.insert(User.class, new User(user.getUsername(), user.getPassword()));
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findOne(User.class, id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User checkLogin(UserRequest user) {
        if (Objects.nonNull(user.getUsername())) {
            User account = userRepository.findUserByUsername(user.getUsername());

            if (Objects.nonNull(account) && Objects.equals(user.getPassword(), account.getPassword()))
                return account;
        }

        return null;
    }
}
