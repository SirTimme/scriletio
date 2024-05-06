package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.GenericEvent;

public interface IPrecondition<T extends GenericEvent> {
	boolean isValid(final T event);
}