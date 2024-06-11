package dev.sirtimme.scriletio.precondition;

import net.dv8tion.jda.api.events.GenericEvent;

public interface IPrecondition<T extends GenericEvent> {
    boolean isValid(final T event);
}