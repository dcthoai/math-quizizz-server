package math.server.controller;

import com.google.gson.Gson;

import math.server.common.Constants;
import math.server.common.RandomQuestion;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.GameResult;
import math.server.dto.response.UserDTO;
import math.server.model.Question;
import math.server.model.Room;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.GameService;
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
    private static final Map<String, String> correctAnswers = new HashMap<>();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();
    private final Gson gson = new Gson();
    private final GameService gameService;

    public GameController() {
        this.gameService = new GameService();
    }

    @EndPoint("/start")
    public BaseResponse startGame(UserSession session, BaseRequest request) {
        Room room = sessionManager.getRoom(session.getCurrentRoom(), false);

        if (Objects.isNull(room) || room.isEmpty())
            return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Not found room or empty room");

        // Notify start game for all users in room
        room.setPlayingGame(true);
        room.notifyAll(gson.toJson(new BaseResponse(request.getAction(), Constants.GAME_TIMEOUT)));
        sessionManager.notifyChangeRoomsData();

        // Send current game information to each user
        room.getUsers().values().forEach(this::updateUserInfoInGame);

        scheduledTasksService.setInterval(() -> playGame(room), Constants.INTERVAL_TASK + room.getRoomID(), Constants.QUESTION_TIMEOUT);
        scheduledTasksService.setTimeout(() -> finishGame(room.getRoomID()), Constants.TIMEOUT_TASK + room.getRoomID(), Constants.GAME_TIMEOUT);

        return null; // Do not send a response message to the client that sent this request if the game start was successful
    }

    private void playGame(Room room) {
        Question question = RandomQuestion.getRandomQuestion();
        String action = "/game/question";
        correctAnswers.put(room.getRoomID(), question.getAnswer());

        BaseResponse response = new BaseResponse(action, gson.toJson(question.getDTO()));
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
                userDTO.setCorrectAnswers(session.getCorrectAnswers());

                gameResult.setUser(userDTO);
                gameResult.setRanking(roomRanking);

                BaseResponse response = new BaseResponse("/game/finish", gameResult);
                session.notify(gson.toJson(response));
                session.resetGameValue();
            });

            gameService.saveGameHistory(users, roomRanking);
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

    private boolean checkAnswer(String questionKey, String answer) {
        try {
            int result = RandomQuestion.evaluateExpression(answer);
            return Objects.equals(correctAnswers.get(questionKey), String.valueOf(result));
        } catch (Exception e) {
            log.error("Cannot check answer", e);
            return false;
        }
    }

    @EndPoint("/answer")
    public BaseResponse answerQuestion(UserSession session, BaseRequest request) {
        BaseResponse response;
        Room room = sessionManager.getRoom(session.getCurrentRoom(), false);

        if (Objects.isNull(room) || room.isEmpty())
            return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Room not found or empty");

        if (checkAnswer(room.getRoomID(), request.getRequest())) {
            response = new BaseResponse(Constants.SUCCESS, true, request.getAction(), "Correct answer");

            room.updateUserPoint(session.getUsername());
            session.updateCorrectAnswers();

            // Update rank for all users in room
            updateRankInRoom(room);

            // Remove old question and next to new question
            scheduledTasksService.shutdownTask(Constants.INTERVAL_TASK + room.getRoomID());
            scheduledTasksService.setInterval(() -> playGame(room), Constants.INTERVAL_TASK + room.getRoomID(), Constants.QUESTION_TIMEOUT);
        } else {
            response = new BaseResponse(Constants.SUCCESS, false, request.getAction(), "Incorrect answer or invalid expression!");
        }

        return response;
    }
}
