package math.server.service.utils;

import math.server.controller.ClientHandler;
import math.server.model.Room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Session of a specific user. <p>
 * Contains information about the user, login status, game information and handler to handle socket communication.
 * @author dcthoai
 */
public class UserSession {

    private static final Logger log = LoggerFactory.getLogger(UserSession.class);
    private final String clientID;
    private Integer userID;
    private String username;
    private Boolean loginState;
    private Integer currentPoint = 0;
    private Integer currentRank = 0;
    private Integer correctAnswers = 0;
    private String currentRoom;
    private ClientHandler clientHandler;

    public UserSession(String clientID) {
        this.clientID = clientID;
        log.info("Create new session for client: {}", clientID);
    }

    public void updateCorrectAnswers() {
        correctAnswers += 1;
    }

    public void resetGameValue() {
        currentPoint = 0;
        currentRank = 0;
        correctAnswers = 0;
        currentRoom = null;
    }

    public void notify(String response) {
        clientHandler.sendMessage(response);
    }

    public void invalidSession() {
        loginState = false;

        if (Objects.nonNull(currentRoom)) {
            Room room = SessionManager.getInstance().getRoom(currentRoom, false);

            if (Objects.nonNull(room))
                room.removeUser(this);
        }

        resetGameValue();
        SessionManager.getInstance().invalidSession(clientID);
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
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
}
