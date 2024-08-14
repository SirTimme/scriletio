package dev.sirtimme.scriletio.listener;

import dev.sirtimme.scriletio.factory.event.IEventCommandFactory;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.GenericEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener<T extends GenericEvent> extends EventListenerBase<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);
    private final EntityManagerFactory entityManagerFactory;
    private final IEventCommandFactory<T> commandFactory;

    public EventListener(final Class<T> clazz, final EntityManagerFactory entityManagerFactory, final IEventCommandFactory<T> commandFactory) {
        super(clazz);
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