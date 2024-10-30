package math.server.controller;

import com.google.gson.Gson;
import math.server.common.Common;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.model.Room;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.utils.ScheduledTasksService;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EndPoint("/api/game")
public class GameController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();
    private final Gson gson = new Gson();

    @EndPoint("/start")
    @SuppressWarnings("unused")
    public BaseResponse startGame(UserSession session, BaseRequest request) {
        session.setCurrentRoom(new Room(Common.generateUniqueID(SessionManager.getUniqueIDs(), 5)));
        session.getCurrentRoom().addUserToRoom(session.getClientID(), session);
        Room room = session.getCurrentRoom();

        if (Objects.nonNull(room) && room.isEmpty())
            return new BaseResponse(Constants.BAD_REQUEST, false, "/game/start", "Not found room or empty room");

        Map<String, UserSession> userSessionMap = room.getUsers();
        List<UserSession> userSessions = new ArrayList<>(userSessionMap.values());

        scheduledTasksService.setInterval(() -> playGame(userSessions, request), Constants.INTERVAL_TASK + room.getRoomID(), Constants.QUESTION_TIMEOUT);
        scheduledTasksService.setTimeout(() -> finishGame(userSessions, room.getRoomID()), Constants.TIMEOUT_TASK + room.getRoomID(), Constants.GAME_TIMEOUT);

        return new BaseResponse(Constants.SUCCESS, true, request.getAction(), "Playing, let choose answer", "Game over");
    }

    private void playGame(List<UserSession> userSessions, BaseRequest request) {
        sendMessageToRoom(userSessions, new BaseResponse(Constants.SUCCESS, true, request.getAction(), "Playing, let choose answer", "Playing, let choose answer"));
    }

    private void finishGame(List<UserSession> userSessions, String UID) {
        scheduledTasksService.shutdownTask(Constants.INTERVAL_TASK + UID);
        scheduledTasksService.shutdownTask(Constants.TIMEOUT_TASK + UID);

        sendMessageToRoom(userSessions, new BaseResponse(Constants.SUCCESS, true, "/game/finish", "Game over", "Game over"));
    }

    private void sendMessageToRoom(List<UserSession> sessions, BaseResponse response) {
        sessions.forEach(session -> {
            ClientHandler clientHandler = session.getClientHandler();

            if (Objects.nonNull(clientHandler)) {
                clientHandler.sendMessage(gson.toJson(response));
            }
        });
    }
}
