package math.server.model;

import com.google.gson.Gson;
import math.server.controller.ClientHandler;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.RoomDTO;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class Room {

    private static final Logger log = LoggerFactory.getLogger(Room.class);
    private final Gson gson = new Gson();
    private final String roomID;
    private final Map<String, UserSession> users;
    private final Map<String, Integer> ranking;
    private Boolean isPlayingGame = false;

    public Room(String roomID) {
        this.roomID = roomID;
        this.users = new HashMap<>();
        this.ranking = new HashMap<>();
        log.info("Created new room: {}", roomID);
    }

    public String getRoomID() {
        return roomID;
    }

    public Map<String, UserSession> getUsers() {
        return users;
    }

    public ClientHandler getUser(String clientID) {
        if (Objects.nonNull(clientID) && users.containsKey(clientID))
            return users.get(clientID).getClientHandler();

        return null;
    }

    public boolean addUserToRoom(String clientID, UserSession userSession) {
        if (Objects.nonNull(clientID) && Objects.nonNull(userSession)) {
            if (users.size() < 5) {
                users.put(clientID, userSession);
                updateRoomData();
                return true;
            } else {
                log.error("This room is full!");
                return false;
            }
        }

        log.error("User ID is null or not found method to execute this user!");
        return false;
    }

    public void updateRoomData() {
        BaseResponse response = new BaseResponse("/room/users/update", getRoomDTO(this));
        notifyAll(gson.toJson(response));
    }

    public boolean removeUser(String clientID) {
        if (Objects.nonNull(clientID)) {
            users.remove(clientID);
            updateRoomData();

            if (users.isEmpty()) {
                SessionManager.getInstance().removeRoom(roomID);
            }

            return true;
        }

        return false;
    }

    public void notifyAll(String response) {
        List<UserSession> userSessions = new ArrayList<>(users.values());
        userSessions.forEach(session -> session.getClientHandler().sendMessage(response));
    }

    public static List<RoomDTO> getRoomDTOs(List<Room> rooms) {
        List<RoomDTO> roomDTOS = new ArrayList<>();

        rooms.forEach(room -> {
            List<UserSession> sessions = new ArrayList<>(room.getUsers().values());
            List<String> users = sessions.stream().map(UserSession::getUsername).collect(Collectors.toList());
            RoomDTO roomDTO = new RoomDTO(room.getRoomID(), room.isPlayingGame(), users);

            roomDTOS.add(roomDTO);
        });

        return roomDTOS;
    }

    public static RoomDTO getRoomDTO(Room room) {
        List<UserSession> sessions = new ArrayList<>(room.getUsers().values());
        List<String> users = sessions.stream().map(UserSession::getUsername).collect(Collectors.toList());

        return new RoomDTO(room.getRoomID(), room.isPlayingGame(), users);
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
