package math.server.repository.impl;

import math.server.model.User;
import math.server.repository.IUserRepository;
import math.server.repository.utils.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SuppressWarnings("unused")
public class UserRepository extends EntityManager<User> implements IUserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public User findUserByUsername(String username) {
        return findOne(User.class, "`username` = ?", List.of(username));
    }
}
