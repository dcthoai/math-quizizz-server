package math.server.controller;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.RoomDTO;
import math.server.model.Room;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EndPoint("/api/room")
@SuppressWarnings("unused")
public class RoomController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final Gson gson = new Gson();

    @EndPoint()
    public BaseResponse getRooms(UserSession session, BaseRequest request) {
        List<Room> rooms = sessionManager.getRooms(false);
        return new BaseResponse(Constants.NO_ACTION, rooms);
    }

    @EndPoint("/available")
    public BaseResponse getAvailableRooms(UserSession session, BaseRequest request) {
        List<Room> rooms = sessionManager.getRooms(true);
        return new BaseResponse(Constants.NO_ACTION, rooms);
    }

    @EndPoint("/new")
    public BaseResponse createRoom(UserSession session, BaseRequest request) {
        Room room = sessionManager.getRoom(null, true);  // Create new room
        session.setCurrentRoom(room);

        List<UserSession> sessions = new ArrayList<>(room.getAllUsers().values());
        List<String> users = sessions.stream().map(UserSession::getUsername).collect(Collectors.toList());
        RoomDTO roomDTO = new RoomDTO(room.getRoomID(), false, users);

        return new BaseResponse(Constants.NO_ACTION, roomDTO);
    }

    @EndPoint("/find")
    public BaseResponse findRoom(UserSession session, String roomID) {
        Room room = sessionManager.getRoom(roomID, false);

        if (Objects.nonNull(room)) {
            return new BaseResponse(Constants.NO_ACTION, room);
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, "/room/find", "Not found room with ID " + roomID);
    }

    @EndPoint("/join")
    public BaseResponse joinRoom(UserSession session, String roomID) {
        Room room = sessionManager.getRoom(roomID, false);

        if (Objects.nonNull(room)) {
            boolean isSuccess = room.addUserToRoom(session.getUserID(), session);

            if (isSuccess) {
                session.setCurrentRoom(room);
                return new BaseResponse(Constants.NO_ACTION, room);
            }

            return new BaseResponse(Constants.BAD_REQUEST, false, "/room/join", "Room is full or missing user info!");
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, "/room/join", "Not found room with ID: " + roomID);
    }

    @EndPoint("/exit")
    public BaseResponse exitRoom(UserSession session, BaseRequest request) {
        Room room = session.getCurrentRoom();

        if (Objects.nonNull(room)) {
            session.setCurrentRoom(null);
            boolean isSuccess = room.removeUser(session.getUserID());

            if (room.isEmpty())
                sessionManager.removeRoom(room.getRoomID());

            return new BaseResponse(Constants.SUCCESS, isSuccess, "/room/exit");
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, "/room/exit", "You don't in which the room");
    }
}
