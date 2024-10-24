package math.server.service.utils;

import math.server.controller.ClientHandler;
import math.server.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSession {

    private static final Logger log = LoggerFactory.getLogger(UserSession.class);
    private final String userID;
    private String username;
    private Boolean loginState;
    private Integer currentPoint;
    private Integer currentRank;
    private Integer correctAnswers;
    private Room currentRoom;
    private ClientHandler clientHandler;

    public UserSession(String userID) {
        this.userID = userID;
        log.info("Create new session for client: {}", userID);
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getLoginState() {
        return loginState;
    }

    public void setLoginState(Boolean loginState) {
        this.loginState = loginState;
    }

    public Integer getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Integer getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(Integer currentRank) {
        this.currentRank = currentRank;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
