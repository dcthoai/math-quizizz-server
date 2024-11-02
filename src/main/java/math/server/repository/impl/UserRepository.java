package math.server.repository.impl;

import math.server.entity.User;
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
        return findOne("`username` = ?", List.of(username));
    }

    @Override
    public int getGamesWinOfUser(Integer userID) {
        String sql = "SELECT COUNT(DISTINCT p.gameID) AS wins FROM `player` p " +
                     "WHERE p.userID = ? AND p.rank = 1 ";

        return (int) count(sql, List.of(userID));
    }

    @Override
    public int getTotalGamesPlayedOfUser(Integer userID) {
        String sql = "SELECT COUNT(DISTINCT p.gameID) AS totalGames " +
                     "FROM `player` p WHERE p.userID = ?";

        return (int) count(sql, List.of(userID));
    }
}
