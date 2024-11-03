package math.server.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class GameHistory {

    private String finishedTime;
    private Integer score;
    private Integer userRank;
    private Integer totalPlayer;

    public GameHistory() {}

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setFinishedTime(Timestamp finishedTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy");
        LocalDateTime localDateTime = finishedTime.toLocalDateTime();
        this.finishedTime = localDateTime.format(dateTimeFormatter);
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUserRank() {
        return userRank;
    }

    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    public Integer getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(Integer totalPlayer) {
        this.totalPlayer = totalPlayer;
    }
}
