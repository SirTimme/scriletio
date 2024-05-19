package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.factories.ICommandFactory;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.GenericEvent;

public class CommandManager<T extends GenericEvent> implements ICommandManager<T> {
    private final EntityManagerFactory entityManagerFactory;
    private final ICommandFactory<T> commandFactory;

    public CommandManager(final EntityManagerFactory entityManagerFactory, final ICommandFactory<T> commandFactory) {
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
            context.getTransaction().rollback();
        } finally {
            context.close();
        }
    }
}