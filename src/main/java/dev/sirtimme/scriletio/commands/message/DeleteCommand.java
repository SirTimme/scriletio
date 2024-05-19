package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.models.DeleteTask;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

import java.util.List;

public class DeleteCommand implements ICommand<MessageDeleteEvent> {
    private final DeleteTaskManager deleteTaskManager;
    private final IRepository<DeleteConfig> configRepository;
    private final IRepository<DeleteTask> taskRepository;

    public DeleteCommand(final DeleteTaskManager deleteTaskManager, final IRepository<DeleteConfig> configRepository, final IRepository<DeleteTask> taskRepository) {
        this.deleteTaskManager = deleteTaskManager;
        this.configRepository = configRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void execute(final MessageDeleteEvent event) {
        final var deleteConfig = configRepository.get(event.getChannel().getIdLong());
        if (deleteConfig == null) {
            return;
        }

        final var deleteTask = taskRepository.get(event.getMessageIdLong());
        if (deleteTask != null) {
            deleteTaskManager.cancelTask(deleteTask);
            taskRepository.delete(deleteTask);
        }
    }

    @Override
    public List<IPrecondition<MessageDeleteEvent>> getPreconditions() {
        return List.of();
    }
}