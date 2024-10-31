package math.server.dto.response;

import java.util.Map;

public class GameResult {

    private UserDTO user;
    private Map<String, Integer> ranking;

    public GameResult() {}

    @SuppressWarnings("unused")
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @SuppressWarnings("unused")
    public Map<String, Integer> getRanking() {
        return ranking;
    }

    public void setRanking(Map<String, Integer> ranking) {
        this.ranking = ranking;
    }
}
