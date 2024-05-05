package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.PreconditionResult;
import net.dv8tion.jda.api.events.GenericEvent;

import java.util.List;

public interface ICommand<T extends GenericEvent> {
	void execute(final T event);

	default PreconditionResult checkPreconditions(final T event) {
		for (final var precondition : getPreconditions()) {
			if (precondition.check(event) == PreconditionResult.FAILURE) {
				return PreconditionResult.FAILURE;
			}
		}
		return PreconditionResult.SUCCESS;
	}

	List<IPrecondition<T>> getPreconditions();
}