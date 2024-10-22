package math.server.controller;

import com.google.gson.Gson;
import math.server.dto.response.BaseResponse;
import math.server.model.Room;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@EndPoint("/api/room")
@SuppressWarnings("unused")
public class RoomController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();

    @EndPoint("/new")
    public BaseResponse<?> createRoom(UserSession session) {
        Room room = sessionManager.getRoom(null, true);
        session.setCurrentRoom(room);

        return new BaseResponse<>(200, true, "/room/new", "Create new room successful");
    }

    @EndPoint("/find")
    public BaseResponse<?> findRoom(UserSession session, String roomID) {
        Room room = sessionManager.getRoom(roomID, false);

        if (Objects.nonNull(room)) {
            Gson gson = new Gson();
            return new BaseResponse<>(200, true, "/room/find", gson.toJson(room));
        }

        return new BaseResponse<>(400, false, "/room/find", "Not found room with ID " + roomID);
    }

    @EndPoint("/join")
    public BaseResponse<?> joinRoom(UserSession session, String roomID) {
        Room room = sessionManager.getRoom(roomID, false);

        if (Objects.nonNull(room)) {
            boolean isSuccess = room.addNewUserToRoom(session.getUserID(), session.getClientHandler());

            if (isSuccess) {
                session.setCurrentRoom(room);
                return new BaseResponse<>(200, true, "/room/join");
            }

            return new BaseResponse<>(400, false, "/room/join", "Room is full or missing user info!");
        }

        return new BaseResponse<>(400, false, "/room/join", "Not found room with ID: " + roomID);
    }

    @EndPoint("/exit")
    public BaseResponse<?> exitRoom(UserSession session) {
        Room room = session.getCurrentRoom();

        if (Objects.nonNull(room)) {
            session.setCurrentRoom(null);
            boolean isSuccess = room.removeUser(session.getUserID());

            if (room.isEmpty())
                sessionManager.removeRoom(room.getRoomId());

            return new BaseResponse<>(200, isSuccess, "/room/exit");
        }

        return new BaseResponse<>(400, false, "/room/exit", "You don't in which the room");
    }
}
