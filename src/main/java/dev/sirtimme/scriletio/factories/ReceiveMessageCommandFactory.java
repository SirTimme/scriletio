package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.event.MessageReceiveCommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReceiveMessageCommandFactory implements ICommandFactory<MessageReceivedEvent> {
    private final DeleteTaskManager deleteTaskManager;

    public ReceiveMessageCommandFactory(final DeleteTaskManager deleteTaskManager) {
        this.deleteTaskManager = deleteTaskManager;
    }

    @Override
    public ICommand<MessageReceivedEvent> createCommand(final MessageReceivedEvent event, final EntityManager context) {
        return new MessageReceiveCommand(deleteTaskManager, new DeleteConfigRepository(context));
    }
}