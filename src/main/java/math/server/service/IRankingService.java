package math.server.service;

import math.server.dto.response.RankDTO;

import java.util.List;
import java.util.Map;

public interface IRankingService {

    void updateRanking(Map<String, Integer> ranking);

    List<RankDTO> getAllUserRanks();

    Integer getUserRank(Integer userID);
}
