package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteJobManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

import java.util.List;

public class DeleteCommand implements ICommand<MessageDeleteEvent> {
	private final DeleteJobManager deleteJobManager;
	private final IRepository<DeleteConfig> repository;

	public DeleteCommand(final DeleteJobManager deleteJobManager, final IRepository<DeleteConfig> repository) {
		this.deleteJobManager = deleteJobManager;
		this.repository = repository;
	}

	@Override
	public void execute(final MessageDeleteEvent event) {
		final var deleteConfig = repository.get(event.getChannel().getIdLong());
		if (deleteConfig == null) {
			return;
		}

		deleteJobManager.cancelJob(event.getMessageIdLong());
	}

	@Override
	public List<IPrecondition<MessageDeleteEvent>> getPreconditions() {
		return List.of();
	}
}