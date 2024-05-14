package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.GenericEvent;

public interface ICommandFactory<T extends GenericEvent> {
	ICommand<T> createCommand(final T event, final EntityManager context);
}
