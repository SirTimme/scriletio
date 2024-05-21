package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

import java.util.List;

public class DeleteCommand implements ICommand<MessageDeleteEvent> {
    private final DeleteTaskManager deleteTaskManager;
    private final IRepository<DeleteConfig> configRepository;

    public DeleteCommand(final DeleteTaskManager deleteTaskManager, final IRepository<DeleteConfig> configRepository) {
        this.deleteTaskManager = deleteTaskManager;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final MessageDeleteEvent event) {
        final var deleteConfig = configRepository.get(event.getChannel().getIdLong());
        if (deleteConfig == null) {
            return;
        }

        final var deleteTask = deleteConfig.getTask(event.getMessageIdLong());
        if (deleteTask != null) {
            deleteTaskManager.cancelTask(deleteTask);
            deleteConfig.getDeleteTasks().remove(deleteTask);
        }
    }

    @Override
    public List<IPrecondition<MessageDeleteEvent>> getPreconditions() {
        return List.of();
    }
}