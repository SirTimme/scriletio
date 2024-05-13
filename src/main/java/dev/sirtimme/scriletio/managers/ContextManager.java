package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ICommand;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.GenericEvent;

public abstract class ContextManager<T extends GenericEvent> {
	private final EntityManagerFactory entityManagerFactory;

	protected ContextManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public void handleCommand(final T event) {
		final var context = entityManagerFactory.createEntityManager();
		final var command = getCommand(event, context);

		if (command.hasInvalidPreconditions(event)) {
			return;
		}

		try {
			context.getTransaction().begin();
			command.execute(event);
			context.getTransaction().commit();
		} catch (Exception error) {
			context.getTransaction().rollback();
		} finally {
			context.close();
		}
	}

	protected abstract ICommand<T> getCommand(final T event, final EntityManager context);
}
