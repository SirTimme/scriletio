package dev.sirtimme.scriletio.managers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DeleteJobManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteJobManager.class);
    private final ConcurrentMap<Long, ScheduledFuture<?>> pendingJobs;

    public DeleteJobManager() {
        this.pendingJobs = new ConcurrentHashMap<>();
    }

    public void cancelJob(final long jobId) {
        final var deleteJob = pendingJobs.get(jobId);

        if (deleteJob != null) {
            deleteJob.cancel(true);
            pendingJobs.remove(jobId);
        }
    }

    public void submitJob(final MessageReceivedEvent event, final long minutes) {
        final var jobId = event.getMessageIdLong();

        final var scheduledJob = event
            .getMessage()
            .delete()
            .queueAfter(
                minutes,
                TimeUnit.MINUTES,
                null,
                error -> LOGGER.error("Message deletion failed: {}", error.getMessage())
            );

        pendingJobs.put(jobId, scheduledJob);
    }
}