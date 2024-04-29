package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.concurrent.DeleteJobManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class DeleteCommand {
	private final DeleteJobManager deleteJobManager;
	private final IRepository<DeleteConfig> repository;

	public DeleteCommand(final DeleteJobManager deleteJobManager, final IRepository<DeleteConfig> repository) {
		this.deleteJobManager = deleteJobManager;
		this.repository = repository;
	}

	public void execute(final MessageDeleteEvent event) {
		final var deleteConfig = repository.get(event.getChannel().getIdLong());
		if (deleteConfig == null) {
			return;
		}

		deleteJobManager.cancelJob(event.getMessageIdLong());
	}
}