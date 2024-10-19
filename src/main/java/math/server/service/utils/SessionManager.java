package main.java.math.server.service.utils;

import main.java.math.server.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SessionManager {

    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
    private static final SessionManager instance = new SessionManager();
    private final Map<String, Room> rooms;
    private final Map<String, UserSession> sessions;

    private SessionManager() {
        this.sessions = new HashMap<>();
        this.rooms = new HashMap<>();
        log.info("Initialized session manager successfully.");
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public Room getRoom(String roomID, Boolean createRoom) {
        if (rooms.containsKey(roomID))  // If it has a room valid
            return rooms.get(roomID);

        if (createRoom) {   // Create new room if required
            Room room = new Room();
            rooms.put(room.getRoomId(), room);
            return room;
        }

        return null;
    }

    public UserSession getSession() {
        String userID = UUID.randomUUID().toString();

        if (sessions.containsKey(userID))
            return sessions.get(userID);    // If user has a session
        else {
            UserSession userSession = new UserSession(userID);
            sessions.put(userID, userSession);
            return userSession;
        }
    }

    public UserSession getSession(String userID, Boolean createSession) {
        if (sessions.containsKey(userID))
            return sessions.get(userID);    // If user has a session

        if (createSession && Objects.nonNull(userID)) {     // Create new session for this user if required
            UserSession userSession = new UserSession(userID);
            sessions.put(userID, userSession);
            return userSession;
        }

        return null;
    }

    public void invalidSession(String userId) {
        sessions.remove(userId);
    }
}
