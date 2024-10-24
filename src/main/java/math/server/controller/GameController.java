package math.server.controller;

import math.server.common.Constants;
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
@SuppressWarnings("unused")
public class GameController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();

    @EndPoint("/start")
    public BaseResponse<?> startGame(UserSession session) {
        Room room = session.getCurrentRoom();

        if (Objects.nonNull(room) && room.isEmpty())
            return new BaseResponse<>(400, false, "/game/start", "Not found room or empty room");

        Map<String, UserSession> userSessionMap = room.getAllUsers();
        List<UserSession> userSessions = new ArrayList<>(userSessionMap.values());

        scheduledTasksService.setInterval(() -> playGame(userSessions), Constants.QUESTION_TIMEOUT);
        scheduledTasksService.setTimeout(() -> finishGame(userSessions), Constants.GAME_TIMEOUT);

        return new BaseResponse<>(200, true, "/game/start");
    }

    private void playGame(List<UserSession> userSessions) {

    }

    private void finishGame(List<UserSession> userSessions) {

    }
}
