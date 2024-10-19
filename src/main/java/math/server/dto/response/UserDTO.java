package main.java.math.server.dto.response;

public class UserDTO {

    private String username;
    private Long currentPoint, totalPoint;
    private int currentRank;
    private boolean loginStatus;

    public UserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Long currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }
}
