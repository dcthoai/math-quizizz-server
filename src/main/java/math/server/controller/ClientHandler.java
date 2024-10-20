package main.java.math.server.controller;

import com.google.gson.Gson;
import main.java.math.server.common.Constants;
import main.java.math.server.dto.request.BaseRequest;
import main.java.math.server.dto.response.BaseResponse;
import main.java.math.server.model.Room;
import main.java.math.server.router.Router;
import main.java.math.server.service.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ClientHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final Router router;
    private final SessionManager sessionManager;
    private final Socket socket;
    private final String clientID;
    private BufferedReader requestReader;
    private PrintWriter responseWriter;

    public ClientHandler(Socket socket, Router router, SessionManager sessionManager) {
        this.socket = socket;
        this.router = router;
        this.sessionManager = sessionManager;
        this.clientID = UUID.randomUUID().toString();
        log.info("Initialize new socket connection successfully. Running for client: {}", clientID);

        try {
            this.requestReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.responseWriter = new PrintWriter(socket.getOutputStream(), true);  // Auto flush data
            log.info("Initialize socket connection IO stream successfully");
        } catch (IOException e) {
            log.error("Failed to init IO stream for socket {}: ", socket.getInetAddress(), e);
        }
    }

    @Override
    public void run() {
        log.info("Initialize client handler successfully. Listening message...");
        Gson gson = new Gson();

        while (true) {
            try {
                String jsonRequest = requestReader.readLine();

                if (Objects.isNull(jsonRequest)) {
                    log.warn("Client disconnected");
                    closeConnection();
                    return;
                }

                BaseRequest request = gson.fromJson(jsonRequest, BaseRequest.class);

                if (Objects.isNull(request.getEndPoint()))
                    continue;

                if (request.getEndPoint().equals(Constants.SOCKET_CLOSE))
                    return;

                BaseResponse<?> response = (BaseResponse<?>) router.handleRequest(request.getEndPoint(), request.getRequest());
                responseWriter.println(gson.toJson(response));
            } catch (SocketException e) {
                log.warn("Socket connection error. Client might be disconnected: ", e);
                closeConnection();
                return;
            } catch (IOException e) {
                log.error("IO error occurred while reading request: ", e);
                closeConnection();
                return;
            } catch (Exception e) {
                log.error("Failed to execute request from client: ", e);
                BaseResponse<?> response = new BaseResponse<>(500, false);
                responseWriter.println(gson.toJson(response));
            }
        }
    }

    public String getClientID() {
        return clientID;
    }

    public void sendMessage(String message) {
        log.info("Send message for: {}", clientID);
        responseWriter.println(message);
    }

    public void sendMessageForUserInRoom(String message, String roomID) {
        Room room = sessionManager.getRoom(roomID, false);

        if (Objects.nonNull(room)) {
            Map<String, ClientHandler> userClientHandlerInRoom = room.getAllUser();

            userClientHandlerInRoom.forEach((userID, userClientHandler) -> {
                userClientHandler.sendMessage(message);
            });
        } else {
            log.error("Room is null. Cannot send message for users in room.");
        }
    }

    public void closeConnection() {
        log.info("Close socket connection {}...", socket.getInetAddress());

        try {
            socket.close();
            log.info("Close connection successfully. Client {} is disconnected.", clientID);
        } catch (IOException e) {
            log.error("Failed to close socket connection {}: " + e.getMessage(), socket.getInetAddress());
        }
    }
}
