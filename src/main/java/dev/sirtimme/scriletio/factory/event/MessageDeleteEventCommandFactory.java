package dev.sirtimme.scriletio.factory.event;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.event.MessageDeleteCommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class MessageDeleteEventCommandFactory implements IEventCommandFactory<MessageDeleteEvent> {
    private final DeleteTaskManager deleteTaskManager;

    public MessageDeleteEventCommandFactory(final DeleteTaskManager deleteTaskManager) {
        this.deleteTaskManager = deleteTaskManager;
    }

    @Override
    public ICommand<MessageDeleteEvent> createCommand(final MessageDeleteEvent event, final EntityManager context) {
        return new MessageDeleteCommand(deleteTaskManager, new DeleteConfigRepository(context));
    }
}