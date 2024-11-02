package math.server.service.impl;

import math.server.dto.response.RankDTO;
import math.server.entity.Rank;
import math.server.repository.impl.RankingRepository;
import math.server.service.IRankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RankingService implements IRankingService {

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);
    private final RankingRepository rankingRepository;

    public RankingService() {
        this.rankingRepository = new RankingRepository();
    }

    @Override
    public void updateRanking(Map<String, Integer> ranking) {
        ranking.forEach((username, point) -> {
            CompletableFuture.runAsync(() -> {
                rankingRepository.updateRankingByUsername(username, point);
            }).exceptionally(e -> {
                log.error("Failed to update ranking for user: {}", username, e);
                return null;
            });
        });
    }

    @Override
    public List<RankDTO> getAllUserRanks() {
        return rankingRepository.getAllRanking();
    }

    @Override
    public Integer getUserRank(Integer userID) {
        return rankingRepository.getUserRank(userID);
    }
}
