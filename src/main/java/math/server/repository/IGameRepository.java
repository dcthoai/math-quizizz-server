package math.server.repository;

import math.server.entity.Game;

import java.util.List;

public interface IGameRepository {

    List<Game> findAllByUser(String username);

    List<Game> findAllByUser(Integer userID);
}
