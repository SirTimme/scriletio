package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.iuvo.commands.event.IEventCommand;
import dev.sirtimme.iuvo.repository.Repository;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.DeleteTask;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class MessageReceiveCommand implements IEventCommand<MessageReceivedEvent> {
    private final DeleteTaskManager deleteTaskManager;
    private final Repository<DeleteConfig> configRepository;

    public MessageReceiveCommand(final DeleteTaskManager deleteTaskManager, final Repository<DeleteConfig> configRepository) {
        this.deleteTaskManager = deleteTaskManager;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final MessageReceivedEvent event) {
        final var deleteConfig = configRepository.get(event.getChannel().getIdLong());

        if (deleteConfig == null) {
            return;
        }

        if (event.getMessage().getType() == MessageType.CHANNEL_PINNED_ADD) {
            final var msgReference = event.getMessage().getMessageReference();
            // noinspection DataFlowIssue since this is a pinned message notification there is always a message reference
            final var deleteTask = deleteConfig.getTask(msgReference.getMessageIdLong());

            // pinned messages could be older than the bot tracks
            if (deleteTask == null) {
                return;
            }

            deleteTaskManager.cancelTask(deleteTask);
            deleteConfig.getDeleteTasks().remove(deleteTask);
        }

        final var deletedAt = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(deleteConfig.getDuration()));
        final var deleteTask = new DeleteTask(deleteConfig, event.getMessageIdLong(), deletedAt);

        deleteTaskManager.submitTask(deleteTask, event.getMessage());
        deleteConfig.getDeleteTasks().add(deleteTask);
    }
}