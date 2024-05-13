package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.message.DeleteCommand;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class MessageDeleteManager extends ContextManager<MessageDeleteEvent> {
	private final DeleteJobManager deleteJobManager;

	public MessageDeleteManager(final EntityManagerFactory entityManagerFactory, final DeleteJobManager deleteJobManager) {
		super(entityManagerFactory);

		this.deleteJobManager = deleteJobManager;
	}

	@Override
	protected ICommand<MessageDeleteEvent> getCommand(final MessageDeleteEvent event, final EntityManager context) {
		return new DeleteCommand(deleteJobManager, new DeleteConfigRepository(context));
	}
}
