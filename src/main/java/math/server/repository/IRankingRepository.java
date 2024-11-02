package math.server.repository;

import math.server.dto.response.RankDTO;

import java.util.List;

public interface IRankingRepository {

    void updateRankingByUsername(String username, Integer point);

    List<RankDTO> getAllRanking();

    RankDTO getRankByUserID(Integer userID);

    RankDTO getRankByUsername(String username);

    int getUserRank(Integer userID);
}
