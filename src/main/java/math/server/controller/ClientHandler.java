package main.java.math.server.controller;

import com.google.gson.Gson;
import main.java.math.server.dto.request.BaseRequest;
import main.java.math.server.dto.response.BaseResponse;
import main.java.math.server.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;

public class ClientHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final Router router;
    private final Socket socket;
    private final String clientID;
    private BufferedReader requestReader;
    private PrintWriter responseWriter;

    public ClientHandler(Socket socket) {
        log.info("Initialized ClientHandler successfully.");
        this.socket = socket;
        this.router = new Router();
        this.clientID = UUID.randomUUID().toString();

        try {
            this.requestReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.responseWriter = new PrintWriter(socket.getOutputStream(), true);  // Auto flush data
        } catch (IOException e) {
            log.error("Failed to init IO stream from socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            log.info("Socket connection is running for client: {}", clientID);
            Gson gson = new Gson();

            while (Objects.nonNull(requestReader.readLine())) {
                String jsonRequest = requestReader.readLine();
                BaseRequest baseRequest = gson.fromJson(jsonRequest, BaseRequest.class);

                BaseResponse<?> response = (BaseResponse<?>) router.handleRequest(baseRequest.getEndPoint(), baseRequest.getRequest());
                responseWriter.println(response);
            }
        } catch (IOException e) {
            log.error("Failed to execute request from client: " + e.getMessage());
        }
    }

    public String getClientID() {
        return clientID;
    }

    public void sendMessage(String message) {
        log.info("Send message for: {}", clientID);
        responseWriter.println(message);
    }

    public void closeConnection() {
        try {
            log.info("Close client {} connection successfully.", clientID);
            socket.close();
        } catch (IOException e) {
            log.error("Failed to close socket connection {}: " + e.getMessage(), socket.getInetAddress());
        }
    }
}
