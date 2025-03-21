package math.server.model;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.RoomDTO;
import math.server.entity.User;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Room {

    private static final Logger log = LoggerFactory.getLogger(Room.class);
    private final Gson gson = new Gson();
    private final String roomID;
    private final Map<String, UserSession> users = new ConcurrentHashMap<>();
    private Map<String, Integer> ranking = new LinkedHashMap<>();
    private Boolean isPlayingGame = false;

    public Room(String roomID) {
        this.roomID = roomID;
        log.info("Created new room: {}", roomID);
    }

    public String getRoomID() {
        return roomID;
    }

    public Map<String, UserSession> getUsers() {
        return users;
    }

    public boolean addUserToRoom(UserSession userSession) {
        if (isFull() || isPlayingGame())
            return false;

        String username = userSession.getUsername();

        if (Objects.nonNull(username)) {
            users.put(username, userSession);
            ranking.put(username, 0); // Init user rank with current point = 0

            updateRoomData();
            return true;
        }

        log.error("UserSession is null or not found method to execute this user!");
        return false;
    }

    public void updateRoomData() {
        BaseResponse response = new BaseResponse("/room/users/update", getRoomDTO(this));
        notifyAll(gson.toJson(response));
    }

    public boolean removeUser(UserSession userSession) {
        if (Objects.nonNull(userSession)) {
            users.remove(userSession.getUsername());

            if (isEmpty()) {
                SessionManager.getInstance().removeRoom(roomID);
            } else {
                updateRoomData();
            }

            // Notify change list rooms for all online users
            SessionManager.getInstance().notifyChangeRoomsData();
            return true;
        }

        return false;
    }

    public void notifyAll(String response) {
        List<UserSession> userSessions = new ArrayList<>(users.values());

        userSessions.forEach(session -> {
            if (Objects.equals(session.getCurrentRoom(), this.getRoomID()))
                session.notify(response);
        });
    }

    public static List<RoomDTO> getRoomDTOs(List<Room> rooms) {
        List<RoomDTO> roomDTOS = new ArrayList<>();

        try {
            rooms.forEach(room -> {
                if (Objects.nonNull(room)) {
                    roomDTOS.add(getRoomDTO(room));
                }
            });
        } catch (Exception e) {
            log.error("Cannot convert to room DTOs", e);
        }

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
        return users.size() >= Constants.ROOM_SIZE;
    }

    public void updateUserPoint(String username) {
        if (Objects.nonNull(username) && ranking.containsKey(username)) {
            Integer currentPoint = ranking.get(username);
            ranking.put(username, currentPoint + Constants.QUESTION_POINT);

            // Sort by value descending
            ranking = ranking.entrySet()
                    .stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1, // Update value for the key if the key already exists
                            LinkedHashMap::new
                    ));
        }
    }

    public Integer getUserPoint(String username) {
        if (Objects.nonNull(username) && ranking.containsKey(username)) {
            return ranking.get(username);
        }

        return 0;
    }

    public Integer getUserRank(String username) {
        int rank = 1;

        for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
            if (username.equals(entry.getKey()))
                return rank;
            rank++;
        }

        return 0;
    }

    public Map<String, Integer> getRanking() {
        return ranking;
    }

    public Boolean isPlayingGame() {
        return isPlayingGame;
    }

    public void setPlayingGame(Boolean playingGame) {
        isPlayingGame = playingGame;
    }
}
