package dev.sirtimme.scriletio.commands.message;

import dev.sirtimme.scriletio.concurrent.DeleteJobManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageManager {
	private final EntityManagerFactory entityManagerFactory;
	private final DeleteJobManager deleteJobManager;

	public MessageManager(final EntityManagerFactory entityManagerFactory, final DeleteJobManager deleteJobManager) {
		this.entityManagerFactory = entityManagerFactory;
		this.deleteJobManager = deleteJobManager;
	}

	public void handleMessageReceive(final MessageReceivedEvent event) {
		final var entityManager = entityManagerFactory.createEntityManager();
		final var command = new ReceiveCommand(deleteJobManager, new DeleteConfigRepository(entityManager));

		entityManager.getTransaction().begin();
		command.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public void handleMessageDelete(final MessageDeleteEvent event) {
		final var entityManager = entityManagerFactory.createEntityManager();
		final var command = new DeleteCommand(deleteJobManager, new DeleteConfigRepository(entityManager));

		entityManager.getTransaction().begin();
		command.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}