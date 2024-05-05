package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.GenericEvent;

public interface IPrecondition<T extends GenericEvent> {
	PreconditionResult check(final T event);
}