package math.server.service.utils;

import math.server.common.Constants;
import math.server.controller.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class to manage socket server connection <p>
 * Listen for connection requests from clients <p>
 * When a client is connected, initiate a separate thread to handle communication with it
 * @author dcthoai
 */
public class ConnectionUtil implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionUtil.class);
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private static final ConnectionUtil instance = new ConnectionUtil();

    private ConnectionUtil() {}

    public static ConnectionUtil getInstance() {
        return instance;
    }

    @Override
    public void run() {
        log.info("Starting socket server...");
        log.info("Create thread pool with 1 thread to execute socket connection");

        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            log.info("Initialize socket server successfully");

            while (true) {
                Socket socket = serverSocket.accept();  // Block the loop and wait for a client to connect
                pool.execute(new ClientHandler(socket));  // Create new Thread to execute this client request.
            }
        } catch (IOException e) {
            log.error("Failed to accept client socket connect", e);
        }
    }
}
