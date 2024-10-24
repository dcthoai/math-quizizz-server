package math.server;

import math.server.common.Constants;
import math.server.router.Router;
import math.server.service.utils.ConnectionUtil;
import math.server.service.utils.ScheduledTasksService;
import math.server.service.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author dcthoai
 */
public class MathServer {

    private static final Logger log = LoggerFactory.getLogger(MathServer.class);
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println(Constants.APP_NAME);
        log.info("Starting server...");

        pool.execute(() -> ConnectionUtil.getInstance().run());
        Router.getInstance().run();
        SessionManager.getInstance().run();
        ScheduledTasksService.getInstance().run();

        log.info("Application is running");
        log.info(
                "\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running!  Access URLs:\n\t" +
                        "Local: \t\tws://localhost:{}\n\t" +
                        "\n----------------------------------------------------------",
                MathServer.class.getSimpleName(),
                Constants.SERVER_PORT
        );
    }
}
