package math.server.common;

/**
 * All configuration constants and parameters used for the application
 * @author dcthoai
 */
@SuppressWarnings("unused")
public interface Constants {

    String DATABASE_HOST = "jdbc:mysql://localhost:3306/math?useSSL=true&requireSSL=false";
    String DATABASE_USER = "admin-math";
    String DATABASE_KEY = "1234";

    Integer MAX_CONNECTION = 10;
    Integer MIN_CONNECTION_IDLE = 3;
    Long CONNECTION_IDLE_TIMEOUT = 5 * 60 * 1000L; // 5 minutes
    Long CONNECTION_TIMEOUT = 30 * 1000L; // 30 seconds
    Long CONNECTION_LIFETIME = 10 * 60 * 1000L; // 10 minutes

    String CONTROLLER_PACKAGE = "math.server.controller";
    String SOCKET_CLOSE = "EXIT";
    Integer SERVER_PORT = 8888;

    String NO_ACTION = "NO_ACTION";
    String NO_CONTENT = "NO_CONTENT";
    Integer SUCCESS = 200;
    Integer BAD_REQUEST = 400;
    Integer UNAUTHORIZED = 401;
    Integer FORBIDDEN = 403;
    Integer NOT_FOUND = 404;
    Integer INTERNAL_SERVER_ERROR = 500;
    Integer SOCKET_CONNECT_SUCCESS = 1000;
    Integer SOCKET_DISCONNECT = 1001;
    Integer SOCKET_CONNECT_TIMEOUT = 1002;
    Integer SOCKET_SENT_DATA_SUCCESS = 2000;
    Integer SOCKET_INVALID_DATA = 4000;
    Integer UNKNOWN_SOCKET_SERVER = 5000;

    Integer FRIENDSHIP_REFUSED = 0;
    Integer FRIENDSHIP_ACCEPTED = 1;
    Integer FRIENDSHIP_PENDING = 2;

    Integer QUESTION_POINT = 1000;
    Long QUESTION_TIMEOUT = 30 * 1000L; // 30 seconds
    Long GAME_TIMEOUT = 2 * 30 * 1000L; // 5 minutes equivalent to 10 question
    String TIMEOUT_TASK = "TIMEOUT_";
    String INTERVAL_TASK = "INTERVAL_";

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";

    String APP_NAME = "\n" +
        ANSI_GREEN + "\n\t ██╗     ██╗" + ANSI_RED + "    ██╗    ████████╗ ██╗   ██╗      ██████╗ ████████╗ ███████╗  ██╗    ██╗ ████████╗ ███████╗    " + ANSI_RESET +
        ANSI_GREEN + "\n\t ███╗   ███║" + ANSI_RED + "  █║   █╗  ╚══██╔══╝ ██║   ██║     ██╔════╝ ██╔═════╝ ██╔═══██╗ ██║    ██║ ██╔═════╝ ██╔═══██╗   " + ANSI_RESET +
        ANSI_GREEN + "\n\t ██║█╗ █╝██║" + ANSI_RED + " ████████╗    ██║    ████████║     ╚█████╗  ██████╗   ███████╔╝ ██║    ██║ ██████╗   ███████╔╝   " + ANSI_RESET +
        ANSI_GREEN + "\n\t ██║ ██╝ ██║" + ANSI_RED + " ██╔═══██║    ██║    ██╔═══██║      ╚═══██╗ ██╔═══╝   ██╔══██║   ██║  ██║  ██╔═══╝   ██╔══██║    " + ANSI_RESET +
        ANSI_GREEN + "\n\t ██║     ██║" + ANSI_RED + " ██║   ██║    ██║    ██║   ██║     ██████╔╝ ████████╗ ██║  ╚██╗   ╚████║   ████████╗ ██║  ╚██╗   " + ANSI_RESET +
        ANSI_GREEN + "\n\t ╚═╝     ╚═╝" + ANSI_RED + " ╚═╝   ╚═╝    ╚═╝    ╚═╝   ╚═╝     ╚═════╝  ╚═══════╝ ╚═╝   ╚═╝    ╚══╝    ╚═══════╝ ╚═╝   ╚═╝ \n" + ANSI_RESET;
}
