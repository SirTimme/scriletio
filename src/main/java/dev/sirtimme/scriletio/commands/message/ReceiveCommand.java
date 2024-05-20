package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.models.DeleteTask;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReceiveCommand implements ICommand<MessageReceivedEvent> {
    private final DeleteTaskManager deleteTaskManager;
    private final IRepository<DeleteConfig> configRepository;
    private final IRepository<DeleteTask> taskRepository;

    public ReceiveCommand(
        final DeleteTaskManager deleteTaskManager,
        final IRepository<DeleteConfig> configRepository,
        final IRepository<DeleteTask> taskRepository
    ) {
        this.deleteTaskManager = deleteTaskManager;
        this.configRepository = configRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void execute(final MessageReceivedEvent event) {
        final var deleteConfig = configRepository.get(event.getChannel().getIdLong());
        if (deleteConfig == null) {
            return;
        }

        if (event.getMessage().getType() == MessageType.CHANNEL_PINNED_ADD) {
            final var msgReference = event.getMessage().getMessageReference();
            final var deleteTask = taskRepository.get(msgReference.getMessageIdLong());

            deleteTaskManager.cancelTask(deleteTask);
            taskRepository.delete(deleteTask);
        }

        final var deletedAt = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(deleteConfig.getDuration()));
        final var deleteTask = new DeleteTask(event.getChannel().getIdLong(), event.getMessageIdLong(), deletedAt);

        deleteTaskManager.submitTask(deleteTask, event.getMessage());
        taskRepository.add(deleteTask);
    }

    @Override
    public List<IPrecondition<MessageReceivedEvent>> getPreconditions() {
        return List.of();
    }
}