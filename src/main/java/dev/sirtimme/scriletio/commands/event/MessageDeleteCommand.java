package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class MessageDeleteCommand implements ICommand<MessageDeleteEvent> {
    private final DeleteTaskManager deleteTaskManager;
    private final IRepository<DeleteConfig> configRepository;

    public MessageDeleteCommand(final DeleteTaskManager deleteTaskManager, final IRepository<DeleteConfig> configRepository) {
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

        if (deleteTask == null) {
            return;
        }

        deleteTaskManager.cancelTask(deleteTask);
        deleteConfig.getDeleteTasks().remove(deleteTask);
    }
}