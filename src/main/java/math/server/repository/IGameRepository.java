package math.server.repository;

import math.server.dto.response.GameHistory;

import java.util.List;

public interface IGameRepository {

    List<GameHistory> findAllByUser(Integer userID);
}
