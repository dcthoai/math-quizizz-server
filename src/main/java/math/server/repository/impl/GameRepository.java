package math.server.repository.impl;

import math.server.entity.Game;
import math.server.repository.IGameRepository;
import math.server.repository.utils.EntityManager;

import java.util.List;

public class GameRepository extends EntityManager<Game> implements IGameRepository {

    @Override
    public List<Game> findAllByUser(String username) {
        String sql = "SELECT g.* FROM game g " +
                     "JOIN player p ON g.ID = p.gameID " +
                     "JOIN user u ON u.ID = p.userID " +
                     "WHERE u.username = ?";

        return query(sql, List.of(username));
    }

    @Override
    public List<Game> findAllByUser(Integer userID) {
        String sql = "SELECT g.* FROM game g " +
                "JOIN player p ON g.ID = p.gameID " +
                "WHERE p.userID = ?";

        return query(sql, List.of(userID));
    }
}
