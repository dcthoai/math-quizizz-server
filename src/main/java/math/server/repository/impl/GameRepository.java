package math.server.repository.impl;

import math.server.dto.response.GameHistory;
import math.server.entity.Game;
import math.server.repository.IGameRepository;
import math.server.repository.utils.EntityManager;

import java.util.List;

public class GameRepository extends EntityManager<Game> implements IGameRepository {

    @Override
    public List<GameHistory> findAllByUser(Integer userID) {
        String sql = "SELECT DATE_FORMAT(g.time, '%H:%i %d/%m/%Y') as `finishedTime`, " +
                     "       p.score as `score`, " +
                     "       p.rank as `userRank`, " +
                     "       (SELECT COUNT(*) FROM `player` p2 WHERE p2.gameID = g.ID) as `totalPlayer` " +
                     "FROM `gamehistory` g " +
                     "JOIN `player` p ON g.ID = p.gameID " +
                     "WHERE p.userID = ? " +
                     "GROUP BY g.time, p.score, p.rank, g.ID " +
                     "ORDER BY g.time DESC";

        return query(sql, List.of(userID), GameHistory.class);
    }
}
