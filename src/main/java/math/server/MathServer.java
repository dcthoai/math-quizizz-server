package main.java.math.server;

import main.java.math.server.common.Constants;
import main.java.math.server.controller.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author dcthoai
 */
public class MathServer {

    private static final Logger log = LoggerFactory.getLogger(MathServer.class);
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            log.info("Server initialized successfully. Listening on port: " + Constants.SERVER_PORT);

            do {
                Socket socket = serverSocket.accept();      // Block the loop and wait for a client to connect
                pool.execute(new ClientHandler(socket));    // Create new Thread to execute this client request.
                log.info("New client is connected: " + socket.getInetAddress());
            } while (true);
        } catch (IOException e) {
            log.error("Failed to accept client socket connect: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println(Constants.APP_NAME);
        log.info("Server starting....");
        new MathServer().start();
    }
}
