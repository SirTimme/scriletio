package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.message.DeleteCommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import dev.sirtimme.scriletio.repositories.DeleteTaskRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class DeleteMessageCommandFactory implements ICommandFactory<MessageDeleteEvent> {
    private final DeleteTaskManager deleteTaskManager;

    public DeleteMessageCommandFactory(final DeleteTaskManager deleteTaskManager) {
        this.deleteTaskManager = deleteTaskManager;
    }

    @Override
    public ICommand<MessageDeleteEvent> createCommand(final MessageDeleteEvent event, final EntityManager context) {
        return new DeleteCommand(deleteTaskManager, new DeleteConfigRepository(context), new DeleteTaskRepository(context));
    }
}