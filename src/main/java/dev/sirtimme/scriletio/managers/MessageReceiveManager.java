package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.message.ReceiveCommand;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageReceiveManager extends ContextManager<MessageReceivedEvent> {
	private final DeleteJobManager deleteJobManager;

	public MessageReceiveManager(final EntityManagerFactory entityManagerFactory, final DeleteJobManager deleteJobManager) {
		super(entityManagerFactory);

		this.deleteJobManager = deleteJobManager;
	}

	@Override
	protected ICommand<MessageReceivedEvent> getCommand(final MessageReceivedEvent event, final EntityManager context) {
		return new ReceiveCommand(deleteJobManager, new DeleteConfigRepository(context));
	}
}