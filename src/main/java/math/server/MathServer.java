package math.server;

import math.server.common.Constants;
import math.server.controller.ClientHandler;
import math.server.router.Router;
import math.server.service.utils.SessionManager;
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
    private static SessionManager sessionManager;
    private static Router router;

    public MathServer() {
        log.info("Starting server");
        router = Router.getInstance();
        sessionManager = SessionManager.getInstance();
    }

    public void start() {
        log.info("Starting socket server");

        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            log.info("Initialize socket server successfully. Listening on port: " + Constants.SERVER_PORT);

            do {
                Socket socket = serverSocket.accept();  // Block the loop and wait for a client to connect
                pool.execute(new ClientHandler(socket, router, sessionManager));  // Create new Thread to execute this client request.
            } while (true);
        } catch (IOException e) {
            log.error("Failed to accept client socket connect", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(Constants.APP_NAME);
        new MathServer().start();
    }
}
