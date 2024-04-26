package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.*;

public class MessageManager {
	private final ConcurrentMap<Long, ScheduledFuture<?>> pendingTasks;
	private final ScheduledExecutorService executorService;
	private final EntityManagerFactory entityManagerFactory;

	public MessageManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.pendingTasks = new ConcurrentHashMap<>();
		this.executorService = Executors.newScheduledThreadPool(5);
	}

	public void handleMessageReceive(final MessageReceivedEvent event) {
		final var entityManager = entityManagerFactory.createEntityManager();
		final var repository = new DeleteConfigRepository(entityManager);

		final var deleteConfig = repository.get(event.getChannel().getIdLong());
		if (deleteConfig == null) {
			return;
		}

		if (event.getMessage().getType() == MessageType.CHANNEL_PINNED_ADD) {
			final var msgReference = event.getMessage().getMessageReference();
			final var msgId = msgReference.getMessageIdLong();
			final var deleteJob = pendingTasks.get(msgId);

			if (deleteJob != null) {
				deleteJob.cancel(true);
				pendingTasks.remove(msgId);
			}
		}

		final var deleteMessageJob = new Runnable() {
			@Override
			public void run() {
				event.getMessage().delete().queue();
				pendingTasks.remove(event.getMessageIdLong());
			}
		};

		final var future = executorService.schedule(deleteMessageJob, deleteConfig.getDuration(), TimeUnit.MINUTES);
		pendingTasks.put(event.getMessageIdLong(), future);
	}

	public void handleMessageDelete(final MessageDeleteEvent event) {
		final var entityManager = entityManagerFactory.createEntityManager();
		final var repository = new DeleteConfigRepository(entityManager);
		
		final var deleteConfig = repository.get(event.getChannel().getIdLong());
		if (deleteConfig == null) {
			return;
		}

		final var msgId = event.getMessageIdLong();
		final var deleteJob = pendingTasks.get(msgId);

		if (deleteJob != null) {
			deleteJob.cancel(true);
			pendingTasks.remove(msgId);
		}
	}
}