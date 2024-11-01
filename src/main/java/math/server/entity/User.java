package math.server.entity;

import math.server.dto.response.UserDTO;
import math.server.repository.utils.Entity;
import math.server.service.utils.UserSession;

@Entity(value = "user")
@SuppressWarnings("unused")
public class User {

    private Integer ID, gamesPlayed, score, rank;
    private Float winRate;
    private String username, password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static UserDTO getUserDTO(UserSession session, User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setID(user.getID());
        userDTO.setUsername(user.getUsername());
        userDTO.setRank(user.getRank());
        userDTO.setScore(user.getScore());
        userDTO.setGamesPlayed(user.getGamesPlayed());
        userDTO.setWinRate(user.getWinRate());

        userDTO.setCurrentPoint(session.getCurrentPoint());
        userDTO.setCurrentRank(session.getCurrentRank());
        userDTO.setLoginStatus(true);

        return userDTO;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Float getWinRate() {
        return winRate;
    }

    public void setWinRate(Float winRate) {
        this.winRate = winRate;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
