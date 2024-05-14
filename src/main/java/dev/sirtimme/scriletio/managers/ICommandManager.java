package dev.sirtimme.scriletio.managers;

import net.dv8tion.jda.api.events.GenericEvent;

public interface ICommandManager<T extends GenericEvent> {
	void handleCommand(final T event);
}
