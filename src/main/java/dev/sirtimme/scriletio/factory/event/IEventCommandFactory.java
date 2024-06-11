package dev.sirtimme.scriletio.factory.event;

import dev.sirtimme.scriletio.commands.ICommand;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.GenericEvent;

public interface IEventCommandFactory<T extends GenericEvent> {
    ICommand<T> createCommand(final T event, final EntityManager context);
}