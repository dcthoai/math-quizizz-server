package math.server.service.impl;

import math.server.dto.response.RankDTO;
import math.server.entity.Rank;
import math.server.repository.impl.RankingRepository;
import math.server.service.IRankingService;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RankingService implements IRankingService {

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);
    private final RankingRepository rankingRepository;
    private final UserService userService;

    public RankingService() {
        this.rankingRepository = new RankingRepository();
        this.userService = new UserService();
    }

    @Override
    public Integer addNewUserRank(Integer userID) {
        Rank rank = new Rank();

        rank.setUserID(userID);
        rank.setScore(0);

        return rankingRepository.save(rank);
    }

    @Override
    public void updateRanking(List<UserSession> users, Map<String, Integer> ranking) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        ranking.forEach((username, point) -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                rankingRepository.updateRankingByUsername(username, point)).exceptionally(e -> {
                    log.error("Failed to update ranking for user: {}", username, e);
                    return null;
                });

            futures.add(future);
        });

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> userService.updateGameInfoForAll(users))
                .exceptionally(e -> {
                    log.error("Failed to complete all ranking updates", e);
                    return null;
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
