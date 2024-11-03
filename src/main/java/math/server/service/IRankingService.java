package math.server.service;

import math.server.dto.response.RankDTO;
import math.server.service.utils.UserSession;

import java.util.List;
import java.util.Map;

public interface IRankingService {

    Integer addNewUserRank(Integer userID);

    void updateRanking(List<UserSession> users, Map<String, Integer> ranking);

    List<RankDTO> getAllUserRanks();

    void updateUserRank(Integer userID);
}
