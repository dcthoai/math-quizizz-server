package math.server.repository.impl;

import math.server.dto.response.RankDTO;
import math.server.entity.Rank;
import math.server.repository.IRankingRepository;
import math.server.repository.utils.EntityManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RankingRepository extends EntityManager<Rank> implements IRankingRepository {

    @Override
    public void updateRankingByUsername(String username, Integer score) {
        String sql = "UPDATE `rank` SET `score` = `score` + ? " +
                     "WHERE `userID` in (SELECT `ID` FROM `user` WHERE `username` = ?)";

        update(sql, List.of(score, username));
    }

    @Override
    public List<RankDTO> getAllRanking() {
        String sql = "SELECT r.id, u.username, r.score, RANK() OVER (ORDER BY r.score DESC) AS `userRank` " +
                     "FROM `rank` r JOIN `user` u ON r.userID = u.ID";

        return query(sql, Collections.emptyList(), RankDTO.class);
    }

    @Override
    public RankDTO getRankByUserID(Integer userID) {
        return findOne("`userID` = ?", List.of(userID), RankDTO.class);
    }

    @Override
    public RankDTO getRankByUsername(String username) {
        return findOne("`userID` in (SELECT `ID` FROM `user` WHERE `username` = ?)", List.of(username), RankDTO.class);
    }

    @Override
    public int getUserRank(Integer userID) {
        String sql = " SELECT ranked_data.ID, u.username, ranked_data.score, ranked_data.userRank " +
                     " FROM ( " +
                     "    SELECT `ID`, `userID`, `score`, RANK() OVER (ORDER BY `score` DESC) AS `userRank` " +
                     "    FROM `rank` " +
                     " ) AS ranked_data " +
                     " JOIN `user` u ON ranked_data.userID = u.ID " +
                     " WHERE u.ID = ?";

        List<RankDTO> rankDTOs = query(sql, List.of(userID), RankDTO.class);
        RankDTO rank = rankDTOs.get(0);

        if (Objects.nonNull(rank))
            return rank.getUserRank();

        return 0;
    }
}
