package math.server.common;

public interface Constants {

    String DATABASE_HOST = "jdbc:mysql://localhost:3306/math?useSSL=true&requireSSL=false";
    String DATABASE_USER = "admin-math";
    String DATABASE_KEY = "1234";

    Integer MAX_CONNECTION = 10;
    Integer MIN_CONNECTION_IDLE = 2;
    Long CONNECTION_IDLE_TIMEOUT = 10 * 60 * 1000L; // 10 minutes
    Long CONNECTION_TIMEOUT = 2 * 60 * 1000L; // 2 minutes
    Long CONNECTION_LIFETIME = 20 * 60 * 1000L; // 20 minutes

    String CONTROLLER_PACKAGE = "math.server.controller";
    String SOCKET_CLOSE = "EXIT";
    Integer SERVER_PORT = 8888;

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";

    String APP_NAME = "\n" +
        ANSI_GREEN + "\n\t ██╗     ╔██╗" + ANSI_RED + "    ██╗    ████████╗ ██╗   ██╗      ██████╗ ████████╗ ███████╗  ██╗    ██╗ ████████╗ ███████╗    " + ANSI_RESET +
        ANSI_GREEN + "\n\t ███╗   ╔███║" + ANSI_RED + "  █║   █╗  ╚══██╔══╝ ██║   ██║     ██╔════╝ ██╔═════╝ ██╔═══██╗ ██║    ██║ ██╔═════╝ ██╔═══██╗   " + ANSI_RESET +
        ANSI_GREEN + "\n\t ██║╚███║ ██║" + ANSI_RED + " ████████╗    ██║    ████████║     ╚█████╗  ██████╗   ███████╔╝ ██║    ██║ ██████╗   ███████╔╝   " + ANSI_RESET +
        ANSI_GREEN + "\n\t ██║ ╚═╝  ██║" + ANSI_RED + " ██╔═══██║    ██║    ██╔═══██║      ╚═══██╗ ██╔═══╝   ██╔══██║   ██║  ██║  ██╔═══╝   ██╔══██║    " + ANSI_RESET +
        ANSI_GREEN + "\n\t ██║      ██║" + ANSI_RED + " ██║   ██║    ██║    ██║   ██║     ██████╔╝ ████████╗ ██║  ╚██╗   ╚████║   ████████╗ ██║  ╚██╗   " + ANSI_RESET +
        ANSI_GREEN + "\n\t ╚═╝      ╚═╝" + ANSI_RED + " ╚═╝   ╚═╝    ╚═╝    ╚═╝   ╚═╝     ╚═════╝  ╚═══════╝ ╚═╝   ╚═╝    ╚══╝    ╚═══════╝ ╚═╝   ╚═╝ \n" + ANSI_RESET;
}
