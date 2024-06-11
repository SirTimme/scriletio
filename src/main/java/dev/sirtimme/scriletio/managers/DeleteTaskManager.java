package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.entities.DeleteTask;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DeleteTaskManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTaskManager.class);
    private final ConcurrentMap<Long, ScheduledFuture<?>> pendingTasks;

    public DeleteTaskManager() {
        this.pendingTasks = new ConcurrentHashMap<>();
    }

    public void cancelTask(final DeleteTask deleteTask) {
        final var deleteJob = pendingTasks.get(deleteTask.getMessageId());

        // method gets executed when successfully deleted a message so the job is no longer stored
        if (deleteJob == null) {
            return;
        }

        deleteJob.cancel(true);
        pendingTasks.remove(deleteTask.getMessageId());
    }

    public void submitTask(final DeleteTask deleteTask, final Message message) {
        final var millisecondsRemaining = deleteTask.getDeletedAt().getTime() - System.currentTimeMillis();
        final var minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);

        final var scheduledTask = message
            .delete()
            .queueAfter(
                minutes,
                TimeUnit.MINUTES,
                success -> pendingTasks.remove(message.getIdLong()),
                error -> LOGGER.error("Deletion of message with id {} failed: {}", message.getIdLong(), error.getMessage())
            );

        pendingTasks.put(message.getIdLong(), scheduledTask);
    }
}