package dev.sirtimme.scriletio.messages;

import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageManager {
    private final ScheduledExecutorService executorService;
    private final IRepository<DeleteConfig> repository;

    public MessageManager(final IRepository<DeleteConfig> repository) {
        this.repository = repository;
        this.executorService = Executors.newScheduledThreadPool(5);
    }

    public void handleMessage(final MessageReceivedEvent event) {
        final var deleteConfig = repository.get(event.getChannel().getIdLong());
        if (deleteConfig == null) {
            return;
        }

        executorService.schedule(() -> event.getMessage().delete().queue(), deleteConfig.getDuration(), TimeUnit.MINUTES);
    }
}