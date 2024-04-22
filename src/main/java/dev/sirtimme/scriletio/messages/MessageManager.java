package dev.sirtimme.scriletio.messages;

import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.*;

public class MessageManager {
    private final ConcurrentMap<Long, ScheduledFuture<?>> pendingTasks;
    private final ScheduledExecutorService executorService;
    private final IRepository<DeleteConfig> repository;

    public MessageManager(final IRepository<DeleteConfig> repository) {
        this.pendingTasks = new ConcurrentHashMap<>();
        this.repository = repository;
        this.executorService = Executors.newScheduledThreadPool(5);
    }

    public void handleMessageReceive(final MessageReceivedEvent event) {
        if (event.getMessage().getType() == MessageType.CHANNEL_PINNED_ADD) {
            final var msgReference = event.getMessage().getMessageReference();
            final var msgId = msgReference.getMessageIdLong();

            final var deleteJob = pendingTasks.get(msgId);
            if (deleteJob != null) {
                deleteJob.cancel(true);
                pendingTasks.remove(msgId);
            }
        }

        final var deleteConfig = repository.get(event.getChannel().getIdLong());
        if (deleteConfig == null) {
            return;
        }

        final var deleteMessageJob = new Runnable() {
            @Override
            public void run() {
                event.getMessage().delete().queue();
                pendingTasks.remove(event.getMessageIdLong());
            }
        };

        final var future = executorService.schedule(deleteMessageJob, deleteConfig.getDuration(), TimeUnit.MINUTES);
        pendingTasks.put(event.getMessageIdLong(), future);
    }

    public void handleMessageDelete(final MessageDeleteEvent event) {
        final var msgId = event.getMessageIdLong();

        final var deleteJob = pendingTasks.get(msgId);
        if (deleteJob != null) {
            deleteJob.cancel(true);
            pendingTasks.remove(msgId);
        }
    }
}