package math.server;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author dcthoai
 */
public class MathServer {

    private final Router router;

    public MathServer() {
        this.router = new Router();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            System.out.println("Server is listening on port: " + Constants.SERVER_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new Thread(() -> handleClient(socket, router)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, Router router) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            Gson gson = new Gson();
            String jsonRequest = in.readLine();
            BaseRequest baseRequest = gson.fromJson(jsonRequest, BaseRequest.class);
            Object response = router.handleRequest(baseRequest.getEndPoint(), baseRequest.getRequest());

            out.println(gson.toJson(response));
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MathServer().start();
    }
}
