package math.server.service.utils;

import math.server.common.Common;
import math.server.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SessionManager implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
    private static final Set<String> uniqueIDs = new HashSet<>();
    private static final Map<String, Room> rooms = new HashMap<>();
    private static final Map<String, UserSession> sessions = new HashMap<>();
    private static final SessionManager instance = new SessionManager();

    private SessionManager() {}

    public static SessionManager getInstance() {
        return instance;
    }

    public static Set<String> getUniqueIDs() {
        return uniqueIDs;
    }

    public Room getRoom(String roomID, Boolean createRoom) {
        if (rooms.containsKey(roomID))  // If it has a room valid
            return rooms.get(roomID);

        if (createRoom) {   // Create new room if required
            String newRoomID = Common.generateUniqueID(uniqueIDs, 5);
            Room room = new Room(newRoomID);
            rooms.put(newRoomID, room);
            return room;
        }

        return null;
    }

    public void removeRoom(String roomID) {
        if (Objects.nonNull(roomID))
            rooms.remove(roomID);
    }

    public UserSession getSession() {
        String userID = Common.generateUniqueID(uniqueIDs, 6);
        UserSession userSession = new UserSession(userID);
        sessions.put(userID, userSession);
        return userSession;
    }

    public UserSession getSession(String userID) {
        if (sessions.containsKey(userID))
            return sessions.get(userID);    // If user has a session

        if (Objects.nonNull(userID)) {     // Create new session for this user
            UserSession userSession = new UserSession(userID);
            sessions.put(userID, userSession);
            return userSession;
        }

        return null;
    }

    public UserSession getSession(String username, Boolean createSession) {
        UserSession userSession = sessions.values().stream()
                .filter(session -> session.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (createSession && Objects.isNull(userSession)) {     // Create new session for this user if required
            String userID = Common.generateUniqueID(uniqueIDs, 6);
            UserSession newUserSession = new UserSession(userID);
            sessions.put(userID, newUserSession);
            return newUserSession;
        }

        return userSession;
    }

    public void invalidSession(String userID) {
        sessions.remove(userID);
        log.info("Invalid session for client: {}", userID);
    }

    @Override
    public void run() {
        log.info("Initialize session manager successfully. Number of users online: {}", getInstance().sessions.size());
    }
}
