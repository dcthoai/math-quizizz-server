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

import java.util.List;
import java.util.Objects;

@EndPoint("/api/room")
@SuppressWarnings("unused")
public class RoomController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final Gson gson = new Gson();

    @EndPoint()
    public BaseResponse getRooms(UserSession session, BaseRequest request) {
        List<Room> rooms = sessionManager.getRooms(false);
        return new BaseResponse(request.getAction(), rooms);
    }

    @EndPoint("/available")
    public BaseResponse getAvailableRooms(UserSession session, BaseRequest request) {
        List<Room> rooms = sessionManager.getRooms(true);
        return new BaseResponse(request.getAction(), Room.getRoomDTOs(rooms));
    }

    @EndPoint("/new")
    public BaseResponse createRoom(UserSession session, BaseRequest request) {
        Room room = session.getCurrentRoom();

        if (Objects.isNull(room)) {
            room = sessionManager.getRoom(null, true);  // Create new room if user has no rooms available
            room.addUserToRoom(session.getClientID(), session);
            session.setCurrentRoom(room);
        }

        // Update list rooms for all online users
        sessionManager.notifyChangeRoomsData();

        return new BaseResponse(request.getAction(), Room.getRoomDTO(room));
    }

    @EndPoint("/find")
    public BaseResponse findRoom(UserSession session, BaseRequest request) {
        Room room = sessionManager.getRoom(request.getRequest(), false);

        if (Objects.nonNull(room)) {
            return new BaseResponse(request.getAction(), room.getRoomID());
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Not found room with ID: " + request.getRequest());
    }

    @EndPoint("/join")
    public BaseResponse joinRoom(UserSession session, BaseRequest request) {
        Room room = sessionManager.getRoom(request.getRequest(), false);

        if (Objects.nonNull(room)) {
            boolean isSuccess = room.addUserToRoom(session.getClientID(), session);

            if (isSuccess) {
                session.setCurrentRoom(room);
                return new BaseResponse(request.getAction(), Constants.NO_CONTENT);
            }

            return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Room is full or missing user info!");
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Not found room with ID: " + request.getRequest());
    }

    @EndPoint("/exit")
    public BaseResponse exitRoom(UserSession session, BaseRequest request) {
        Room room = session.getCurrentRoom();

        if (Objects.nonNull(room)) {
            session.setCurrentRoom(null);
            boolean isSuccess = room.removeUser(session.getClientID());

            // Notify change list rooms for all online users
            sessionManager.notifyChangeRoomsData();

            return new BaseResponse(Constants.SUCCESS, isSuccess, request.getAction());
        }

        return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "You are not in this room");
    }
}
