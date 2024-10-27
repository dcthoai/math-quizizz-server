package math.server.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A class to manage scheduled tasks
 * @author dcthoai
 */
public class ScheduledTasksService implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasksService.class);
    private static final ScheduledTasksService instance = new ScheduledTasksService();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    private ScheduledTasksService() {}

    public static ScheduledTasksService getInstance() {
        return instance;
    }

    public void setTimeout(Runnable task, String UID, long delay) {
        ScheduledFuture<?> scheduledFuture;

        if (scheduledTasks.containsKey(UID)) {
            scheduledFuture = scheduledTasks.get(UID);

            // Cancel the old task if it was previously registered
            if (Objects.nonNull(scheduledFuture) && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
            }
        }

        log.info("Scheduling task to run after {} milliseconds", delay);
        // Schedule a task to be executed after a delay of a time equal to delay variable (milliseconds)
        scheduledFuture = scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(UID, scheduledFuture);   // Save scheduled tasks to the map to cancel when needed
    }

    public void setInterval(Runnable task, String UID, long intervalTime) {
        ScheduledFuture<?> scheduledFuture;

        if (scheduledTasks.containsKey(UID)) {
            scheduledFuture = scheduledTasks.get(UID);

            // Cancel the old task if it was previously registered
            if (Objects.nonNull(scheduledFuture) && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
            }
        }

        log.info("Scheduling task to run every {} milliseconds", intervalTime);
        // Schedule a task to repeat every time interval equal to a intervalTime variable (milliseconds)
        scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, intervalTime, TimeUnit.MILLISECONDS);
        scheduledTasks.put(UID, scheduledFuture);   // Save scheduled tasks to the map to cancel when needed
    }

    public void shutdownTask(String UID) {
        if (scheduledTasks.containsKey(UID)) {
            ScheduledFuture<?> scheduledFuture = scheduledTasks.get(UID);

            if (Objects.nonNull(scheduledFuture) && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
            }

            scheduledTasks.remove(UID);
            log.info("Shutdown and remove task with ID: {}", UID);
        }
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

    @Override
    public void run() {
        log.info("Initialize schedule tasks service");
    }
}
