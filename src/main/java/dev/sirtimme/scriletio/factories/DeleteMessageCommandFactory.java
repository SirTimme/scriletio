package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.message.DeleteCommand;
import dev.sirtimme.scriletio.managers.DeleteJobManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class DeleteMessageCommandFactory implements ICommandFactory<MessageDeleteEvent> {
	private final DeleteJobManager deleteJobManager;

	public DeleteMessageCommandFactory(final DeleteJobManager deleteJobManager) {
		this.deleteJobManager = deleteJobManager;
	}

	@Override
	public ICommand<MessageDeleteEvent> createCommand(final MessageDeleteEvent event, final EntityManager context) {
		return new DeleteCommand(deleteJobManager, new DeleteConfigRepository(context));
	}
}
