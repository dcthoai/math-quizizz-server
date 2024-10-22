package math.server.model;

import math.server.controller.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Room {

    private static final Logger log = LoggerFactory.getLogger(Room.class);
    private final String roomId;
    private final Map<String, ClientHandler> clientHandlers;

    public Room() {
        this.roomId = UUID.randomUUID().toString();
        this.clientHandlers = new HashMap<>();
        log.info("Created new room: {}", roomId);
    }

    public String getRoomId() {
        return roomId;
    }

    public Map<String, ClientHandler> getAllUser() {
        return clientHandlers;
    }

    public ClientHandler getOneUser(String userId) {
        if (Objects.nonNull(userId) && clientHandlers.containsKey(userId))
            return clientHandlers.get(userId);

        return null;
    }

    public boolean addNewUserToRoom(String userId, ClientHandler clientHandler) {
        if (Objects.nonNull(userId) && Objects.nonNull(clientHandler)) {
            if (clientHandlers.size() < 5) {
                clientHandlers.put(userId, clientHandler);
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
            clientHandlers.remove(userID);
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        return clientHandlers.isEmpty();
    }
}
