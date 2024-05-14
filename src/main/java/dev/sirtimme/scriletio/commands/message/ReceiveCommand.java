package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteJobManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class ReceiveCommand implements ICommand<MessageReceivedEvent> {
	private final DeleteJobManager deleteJobManager;
	private final IRepository<DeleteConfig> repository;

	public ReceiveCommand(final DeleteJobManager deleteJobManager, final IRepository<DeleteConfig> repository) {
		this.deleteJobManager = deleteJobManager;
		this.repository = repository;
	}

	@Override
	public void execute(final MessageReceivedEvent event) {
		final var deleteConfig = repository.get(event.getChannel().getIdLong());
		if (deleteConfig == null) {
			return;
		}

		if (event.getMessage().getType() == MessageType.CHANNEL_PINNED_ADD) {
			final var msgReference = event.getMessage().getMessageReference();
			final var jobId = msgReference.getMessageIdLong();

			deleteJobManager.cancelJob(jobId);
		}

		deleteJobManager.submitJob(event, deleteConfig.getDuration());
	}

	@Override
	public List<IPrecondition<MessageReceivedEvent>> getPreconditions() {
		return List.of();
	}
}