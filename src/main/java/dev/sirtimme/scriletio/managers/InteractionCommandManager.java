package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.factory.interaction.IInteractionCommandFactory;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InteractionCommandManager<T extends GenericInteractionCreateEvent> extends CommandManager<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionCommandManager.class);
    private final EntityManagerFactory entityManagerFactory;
    private final IInteractionCommandFactory<T> commandFactory;

    public InteractionCommandManager(final Class<T> clazz, final EntityManagerFactory entityManagerFactory, final IInteractionCommandFactory<T> commandFactory) {
        super(clazz);
        this.entityManagerFactory = entityManagerFactory;
        this.commandFactory = commandFactory;
    }

    @Override
    public void handleCommand(final T event) {
        final var context = entityManagerFactory.createEntityManager();
        final var command = commandFactory.createCommand(event, context);

        if (command.hasInvalidPreconditions(event)) {
            return;
        }

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
