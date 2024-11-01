package math.server.service;

import math.server.service.utils.UserSession;

import java.util.Map;

public interface IGameService {

    void saveGameHistory(Map<String, UserSession> users, Map<String, Integer> ranking);
}
