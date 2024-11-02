package math.server.service.impl;

import math.server.entity.Game;
import math.server.entity.Player;
import math.server.repository.impl.GameRepository;
import math.server.repository.impl.PlayerRepository;
import math.server.service.IGameService;
import math.server.service.utils.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GameService implements IGameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService() {
        this.gameRepository = new GameRepository();
        this.playerRepository = new PlayerRepository();
    }

    @Override
    public void saveGameHistory(Map<String, UserSession> users, Map<String, Integer> ranking) {
        CompletableFuture.runAsync(() -> {
            Game game = new Game();
            int gameID = gameRepository.save(game);

            if (gameID > 0) {
                List<Player> players = new ArrayList<>();
                int[] rank = {1};

                ranking.forEach((username, score) -> {
                    Player player = new Player();

                    player.setRank(rank[0]);
                    player.setScore(score);
                    player.setGameID(gameID);
                    player.setUserID(users.get(username).getUserID());

                    players.add(player);
                    rank[0] += 1;
                });

                playerRepository.saveAll(players);
            }
        }).exceptionally(ex -> {
            log.error("Failed to save game history asynchronously", ex);
            return null;
        });
    }
}
