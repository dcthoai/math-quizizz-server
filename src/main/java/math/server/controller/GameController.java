package math.server.controller;

import com.google.gson.Gson;

import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.GameResult;
import math.server.dto.response.UserDTO;
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
import java.util.HashMap;
import java.util.Objects;

@EndPoint("/api/game")
@SuppressWarnings("unused")
public class GameController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();
    private final Gson gson = new Gson();
    private static final Map<String, String> correctAnswers = new HashMap<>();

    @EndPoint("/start")
    @SuppressWarnings("unused")
    public BaseResponse startGame(UserSession session, BaseRequest request) {
        Room room = sessionManager.getRoom(session.getCurrentRoom(), false);

        if (Objects.isNull(room) || room.isEmpty())
            return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Not found room or empty room");

        // Notify start game view for all users in room
        room.setPlayingGame(true);
        room.notifyAll(gson.toJson(new BaseResponse(request.getAction(), Constants.NO_CONTENT)));
        sessionManager.notifyChangeRoomsData();

        // Send current game information to each user
        room.getUsers().values().forEach(this::updateUserInfoInGame);

        scheduledTasksService.setInterval(() -> playGame(room), Constants.INTERVAL_TASK + room.getRoomID(), Constants.QUESTION_TIMEOUT);
        scheduledTasksService.setTimeout(() -> finishGame(room.getRoomID()), Constants.TIMEOUT_TASK + room.getRoomID(), Constants.GAME_TIMEOUT);

        return null; // Not send message response to client if start game successful
    }

    private void playGame(Room room) {
        String correctAnswer = "ok";
        String question = "question" + System.currentTimeMillis();
        correctAnswers.put(room.getRoomID(), correctAnswer);

        BaseResponse response = new BaseResponse(Constants.SUCCESS, true, "/game/question", Constants.NO_CONTENT, question);
        room.notifyAll(gson.toJson(response));
    }

    private void finishGame(String roomID) {
        scheduledTasksService.shutdownTask(Constants.INTERVAL_TASK + roomID);
        scheduledTasksService.shutdownTask(Constants.TIMEOUT_TASK + roomID);

        Room room = sessionManager.getRoom(roomID, false);

        if (Objects.isNull(room))
            return;

        Map<String, UserSession> users = room.getUsers();
        Map<String, Integer> roomRanking = room.getRanking();

        if (Objects.nonNull(users) && !users.isEmpty()) {
            List<UserSession> userSessions = new ArrayList<>(users.values());

            userSessions.forEach(session -> {
                GameResult gameResult = new GameResult();
                UserDTO userDTO = new UserDTO();

                userDTO.setUsername(session.getUsername());
                userDTO.setCurrentPoint(room.getUserPoint(session.getUsername()));
                userDTO.setCurrentRank(room.getUserRank(session.getUsername()));

                gameResult.setUser(userDTO);
                gameResult.setRanking(roomRanking);

                BaseResponse response = new BaseResponse("/game/finish", gameResult);
                session.notify(gson.toJson(response));
                session.resetGameValue();
            });
        }

        sessionManager.removeRoom(roomID);
    }

    private void updateUserInfoInGame(UserSession session) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(session.getUsername());
        userDTO.setCurrentPoint(session.getCurrentPoint());
        userDTO.setCurrentRank(session.getCurrentRank());

        BaseResponse response = new BaseResponse("/game/user/info/update", userDTO);
        session.notify(gson.toJson(response));
    }

    private void updateRankInRoom(Room room) {
        List<UserSession> sessions = new ArrayList<>(room.getUsers().values());

        sessions.forEach(session -> {
            session.setCurrentPoint(room.getUserPoint(session.getUsername()));
            session.setCurrentRank(room.getUserRank(session.getUsername()));
            updateUserInfoInGame(session);
        });
    }

    private boolean checkAnswer(String answer) {

        return answer.equals("ok");
    }

    @EndPoint("/answer")
    public BaseResponse answerQuestion(UserSession session, BaseRequest request) {
        BaseResponse response;
        Room room = sessionManager.getRoom(session.getCurrentRoom(), false);

        if (Objects.isNull(room) || room.isEmpty())
            return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Room not found or empty");

        if (checkAnswer(request.getRequest())) {
            response = new BaseResponse(Constants.SUCCESS, true, request.getAction(), "Correct answer");

            room.updateUserPoint(session.getUsername());
            session.updateCorrectAnswers();

            // Update rank for all users in room
            updateRankInRoom(room);

            // Remove old question and next to new question
            scheduledTasksService.shutdownTask(Constants.INTERVAL_TASK + room.getRoomID());
            scheduledTasksService.setInterval(() -> playGame(room), Constants.INTERVAL_TASK + room.getRoomID(), Constants.QUESTION_TIMEOUT);
        } else {
            response = new BaseResponse(Constants.SUCCESS, false, request.getAction(), "Incorrect answer");
        }

        return response;
    }
}
