package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.preconditions.IPrecondition;
import net.dv8tion.jda.api.events.GenericEvent;

import java.util.List;

public interface ICommand<T extends GenericEvent> {
	void execute(final T event);

	default boolean checkPreconditions(final T event) {
		for (final var precondition : getPreconditions()) {
			if (!precondition.check(event)) {
				return false;
			}
		}
		return true;
	}

	List<IPrecondition<T>> getPreconditions();
}