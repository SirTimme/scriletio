package dev.sirtimme.scriletio.factory.event;

import dev.sirtimme.iuvo.commands.event.IEventCommand;
import dev.sirtimme.iuvo.factory.event.IEventCommandFactory;
import dev.sirtimme.scriletio.commands.event.MessageReceiveCommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageReceiveEventCommandFactory implements IEventCommandFactory<MessageReceivedEvent> {
    private final DeleteTaskManager deleteTaskManager;

    public MessageReceiveEventCommandFactory(final DeleteTaskManager deleteTaskManager) {
        this.deleteTaskManager = deleteTaskManager;
    }

    @Override
    public IEventCommand<MessageReceivedEvent> createCommand(final EntityManager context) {
        return new MessageReceiveCommand(deleteTaskManager, new DeleteConfigRepository(context));
    }
}