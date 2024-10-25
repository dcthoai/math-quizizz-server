package math.server.model;

import math.server.controller.ClientHandler;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Room {

    private static final Logger log = LoggerFactory.getLogger(Room.class);
    private final String roomID;
    private Boolean isPlayingGame = false;
    private final Map<String, UserSession> users;
    private final Map<String, Integer> ranking;

    public Room(String roomID) {
        this.roomID = roomID;
        this.users = new HashMap<>();
        this.ranking = new HashMap<>();
        log.info("Created new room: {}", roomID);
    }

    public String getRoomID() {
        return roomID;
    }

    public Map<String, UserSession> getAllUsers() {
        return users;
    }

    public ClientHandler getUser(String userID) {
        if (Objects.nonNull(userID) && users.containsKey(userID))
            return users.get(userID).getClientHandler();

        return null;
    }

    public boolean addUserToRoom(String userID, UserSession userSession) {
        if (Objects.nonNull(userID) && Objects.nonNull(userSession)) {
            if (users.size() < 5) {
                users.put(userID, userSession);
                return true;
            } else {
                log.error("This room is full!");
                return false;
            }
        }

        log.error("User ID is null or not found method to execute this user!");
        return false;
    }

    public boolean removeUser(String userID) {
        if (Objects.nonNull(userID)) {
            users.remove(userID);
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean isFull() {
        return users.size() < 5;
    }

    public Map<String, Integer> getRanking() {
        return ranking;
    }

    public void updateRanking(List<User> users) {
        users.forEach(user -> {
            ranking.put(user.getUsername(), user.getScore());
        });
    }

    public Boolean isPlayingGame() {
        return isPlayingGame;
    }

    public void setPlayingGame(Boolean playingGame) {
        isPlayingGame = playingGame;
    }
}
