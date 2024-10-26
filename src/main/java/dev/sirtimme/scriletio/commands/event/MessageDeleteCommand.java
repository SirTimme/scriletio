package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.iuvo.api.commands.event.IEventCommand;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class MessageDeleteCommand implements IEventCommand<MessageDeleteEvent> {
    private final DeleteTaskManager deleteTaskManager;
    private final Repository<DeleteConfig> configRepository;

    public MessageDeleteCommand(final DeleteTaskManager deleteTaskManager, final Repository<DeleteConfig> configRepository) {
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