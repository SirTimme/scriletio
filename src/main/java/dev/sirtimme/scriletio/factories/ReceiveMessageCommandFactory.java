package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.message.ReceiveCommand;
import dev.sirtimme.scriletio.managers.DeleteJobManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReceiveMessageCommandFactory implements ICommandFactory<MessageReceivedEvent> {
	private final DeleteJobManager deleteJobManager;

	public ReceiveMessageCommandFactory(final DeleteJobManager deleteJobManager) {
		this.deleteJobManager = deleteJobManager;
	}

	@Override
	public ICommand<MessageReceivedEvent> createCommand(final MessageReceivedEvent event, final EntityManager context) {
		return new ReceiveCommand(deleteJobManager, new DeleteConfigRepository(context));
	}
}