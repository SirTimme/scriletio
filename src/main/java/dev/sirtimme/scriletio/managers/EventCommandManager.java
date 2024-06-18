package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.factory.event.IEventCommandFactory;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.GenericEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventCommandManager<T extends GenericEvent> implements ICommandManager<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventCommandManager.class);
    private final EntityManagerFactory entityManagerFactory;
    private final IEventCommandFactory<T> commandFactory;

    public EventCommandManager(final EntityManagerFactory entityManagerFactory, final IEventCommandFactory<T> commandFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.commandFactory = commandFactory;
    }

    @Override
    public void handleCommand(final T event) {
        final var context = entityManagerFactory.createEntityManager();
        final var command = commandFactory.createCommand(context);

        try {
            context.getTransaction().begin();
            command.execute(event);
            context.getTransaction().commit();
        } catch (Exception error) {
            LOGGER.error("Execution of command {} failed: {}", command, error.getMessage());
            context.getTransaction().rollback();
        } finally {
            context.close();
        }
    }
}