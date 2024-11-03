package math.server.service;

import math.server.dto.response.GameHistory;
import math.server.service.utils.UserSession;

import java.util.List;
import java.util.Map;

public interface IGameService {

    void saveGameHistory(Map<String, UserSession> users, Map<String, Integer> ranking);

    List<GameHistory> getAllUserGameHistories(Integer userID);
}
