package math.server.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTasksService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasksService.class);
    private static final ScheduledTasksService instance = new ScheduledTasksService();
    private final ScheduledExecutorService scheduler;

    public ScheduledTasksService() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public static ScheduledTasksService getInstance() {
        return instance;
    }

    public void setTimeout(Runnable task, long delay) {
        log.info("Scheduling task to run after {} milliseconds", delay);
        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        log.info("Shutting down scheduled tasks service");
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }
}
