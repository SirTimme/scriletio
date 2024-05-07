package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.preconditions.IPrecondition;
import net.dv8tion.jda.api.events.GenericEvent;

import java.util.List;

public interface ICommand<T extends GenericEvent> {
	void execute(final T event);

	List<IPrecondition<T>> getPreconditions();

	default boolean hasInvalidPreconditions(final T event) {
		for (final var precondition : getPreconditions()) {
			if (!precondition.isValid(event)) {
				return true;
			}
		}
		return false;
	}
}