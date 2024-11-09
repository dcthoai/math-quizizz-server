package math.server.controller;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.model.Room;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@EndPoint("/api/room")
@SuppressWarnings("unused")
public class RoomController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final Gson gson = new Gson();

    @EndPoint("/all")
    public BaseResponse getRooms(UserSession session, BaseRequest request) {
        log.debug("Socket request to get all rooms. Endpoint: /api/room/all");
        List<Room> rooms = sessionManager.getRooms(false);

        if (Objects.isNull(rooms) || rooms.isEmpty())
            return new BaseResponse(request.getAction(), new ArrayList<>());

        return new BaseResponse(request.getAction(), rooms);
    }

    @EndPoint("/available")
    public BaseResponse getAvailableRooms(UserSession session, BaseRequest request) {
        log.debug("Socket request to get all available rooms. Endpoint: /api/room/available");
        List<Room> rooms = sessionManager.getRooms(true);

        if (Objects.isNull(rooms))
            return new BaseResponse(request.getAction(), Collections.emptyList());

        return new BaseResponse(request.getAction(), Room.getRoomDTOs(rooms));
    }

    @EndPoint("/new")
    public BaseResponse createRoom(UserSession session, BaseRequest request) {
        log.debug("Socket request to create a new room. Endpoint: /api/room/new");
        Room room = sessionManager.getRoom(session.getCurrentRoom(), true);  // Create new room if user has no rooms available
        session.setCurrentRoom(room.getRoomID());
        room.addUserToRoom(session.getUsername(), session);

        // Update list rooms for all online users
        sessionManager.notifyChangeRoomsData();

        return new BaseResponse(request.getAction(), Room.getRoomDTO(room));
    }

    @EndPoint("/find")
    public BaseResponse findRoom(UserSession session, BaseRequest request) {
        log.debug("Socket request to find a room. Endpoint: /api/room/find");
        Room room = sessionManager.getRoom(request.getRequest(), false);

        if (Objects.nonNull(room)) {
            return new BaseResponse(request.getAction(), room.getRoomID());
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Not found room with ID: " + request.getRequest());
    }

    @EndPoint("/join")
    public BaseResponse joinRoom(UserSession session, BaseRequest request) {
        log.debug("Socket request to join an exist room. Endpoint: /api/room/join");
        Room room = sessionManager.getRoom(request.getRequest(), false);

        if (Objects.nonNull(room)) {
            boolean isSuccess = room.addUserToRoom(session.getUsername(), session);

            if (isSuccess) {
                session.setCurrentRoom(room.getRoomID());
                return new BaseResponse(request.getAction(), Constants.NO_CONTENT);
            }

            return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "This room is full or in game!");
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Not found room with ID: " + request.getRequest());
    }

    @EndPoint("/exit")
    public BaseResponse exitRoom(UserSession session, BaseRequest request) {
        log.debug("Socket request to exit current room. Endpoint: /api/room/exit");
        Room room = sessionManager.getRoom(session.getCurrentRoom(), false);

        if (Objects.nonNull(room)) {
            boolean isSuccess = room.removeUser(session.getUsername());
            return new BaseResponse(Constants.SUCCESS, isSuccess, request.getAction());
        }

        return new BaseResponse(Constants.NOT_FOUND, false, request.getAction(), "Room not found");
    }
}
