package math.server.service.impl;

import math.server.dto.request.UserRequest;
import math.server.dto.response.RankDTO;
import math.server.dto.response.UserDTO;
import math.server.entity.User;
import math.server.repository.impl.RankingRepository;
import math.server.repository.impl.UserRepository;
import math.server.service.IUserService;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RankingRepository rankingRepository;

    public UserService() {
        this.userRepository = new UserRepository();
        this.rankingRepository = new RankingRepository();
    }

    @Override
    public Integer save(UserRequest userRequest) {
        User user = new User(userRequest.getUsername(), userRequest.getPassword());

        user.setRank(0);
        user.setScore(0);
        user.setWinRate(0f);
        user.setGamesPlayed(0);

        return userRepository.save(user);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User checkLogin(UserRequest user) {
        if (Objects.nonNull(user.getUsername())) {
            User account = userRepository.findUserByUsername(user.getUsername());

            if (Objects.nonNull(account) && Objects.equals(user.getPassword(), account.getPassword()))
                return account;
        }

        return null;
    }

    @Override
    public UserDTO getGameInfo(Integer userID) {
        UserDTO userDTO = new UserDTO();
        User user = userRepository.findOne(userID);

        userDTO.setUsername(user.getUsername());
        userDTO.setLoginStatus(true);
        userDTO.setScore(user.getScore());
        userDTO.setRank(user.getRank());
        userDTO.setWinRate(user.getWinRate());
        userDTO.setGamesPlayed(user.getGamesPlayed());

        return userDTO;
    }

    public void updateGameInfoForAll(List<UserSession> users) {
        users.forEach(user -> updateGameInfo(user.getUserID()));
    }

    @Override
    public void updateGameInfo(Integer userID) {
        CompletableFuture.runAsync(() -> {
            int gameWins = userRepository.getGamesWinOfUser(userID);
            int totalGamesPlayed = userRepository.getTotalGamesPlayedOfUser(userID);
            float winRate = (float) gameWins / totalGamesPlayed;

            RankDTO userRank = rankingRepository.getRankByUserID(userID);
            User user = userRepository.findOne(userID);

            user.setWinRate(winRate * 100);
            user.setGamesPlayed(totalGamesPlayed);
            user.setRank(userRank.getUserRank());
            user.setScore(userRank.getScore());

            userRepository.update(user);
        }).exceptionally(e -> {
            log.error("Failed to save user game info async. ", e);
            return null;
        });
    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }
}
