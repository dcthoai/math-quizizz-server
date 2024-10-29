package math.server.controller;

import com.google.gson.Gson;

import math.server.common.Common;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.router.Router;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;

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

/**
 * A class to manage socket connection of a client and forward all requests to the router for processing <p>
 * Provides methods for communicating with clients and managing connections
 * @author dcthoai
 */
public class ClientHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private static final SessionManager sessionManager = SessionManager.getInstance();
    private static final Router router = Router.getInstance();
    private final UserSession session;
    private final String clientID;
    private final Socket socket;
    private BufferedReader requestReader;
    private PrintWriter responseWriter;

    public ClientHandler(Socket socket) {
        this.clientID = Common.generateUniqueID(SessionManager.getUniqueIDs(), 6);
        this.session = sessionManager.getSession(clientID); // Get new session
        this.session.setClientHandler(this);
        this.socket = socket;
        log.info("Initialize new socket connection successfully. Running for client: {}", clientID);

        try {
            this.requestReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.responseWriter = new PrintWriter(socket.getOutputStream(), true);  // Auto flush data
            log.info("Initialize socket connection IO stream successfully");
        } catch (IOException e) {
            log.error("Failed to init IO stream for socket {}", socket.getInetAddress(), e);
        }
    }

    @Override
    public void run() {
        log.info("Initialize client handler successfully. Listening message...");
        Gson gson = new Gson();

        while (true) {
            try {
                String jsonRequest = requestReader.readLine();  // Read request from client as json string

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

                // Use router to process request
                BaseResponse response = (BaseResponse) router.handleRequest(session, request);

                // Response for client
                responseWriter.println(gson.toJson(response));
            } catch (SocketException e) {
                log.error("Socket connection error. Client might be disconnected", e);
                closeConnection();
                return;
            } catch (IOException e) {
                log.error("IO error occurred while reading request", e);
                closeConnection();
                return;
            } catch (Exception e) {
                log.error("Failed to execute request from client", e);
                BaseResponse response = new BaseResponse(
                        Constants.INTERNAL_SERVER_ERROR,
                        false,
                        Constants.NO_ACTION,
                        "Failed to execute request from client"
                );
                responseWriter.println(gson.toJson(response));
            }
        }
    }

    public String getClientID() {
        return clientID;
    }

    public UserSession getSession() {
        return session;
    }

    public void sendMessage(String message) {
        log.info("Send message for: {}", clientID);
        responseWriter.println(message);
    }

    public void closeConnection() {
        log.info("Close socket connection {}...", socket.getInetAddress());
        sessionManager.invalidSession(clientID);

        try {
            socket.close();
            log.info("Close connection successfully. Client {} is disconnected.", clientID);
        } catch (IOException e) {
            log.error("Failed to close socket connection {}: " + e.getMessage(), socket.getInetAddress());
        }
    }
}
