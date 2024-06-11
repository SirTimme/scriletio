package dev.sirtimme.scriletio.commands;

import net.dv8tion.jda.api.events.GenericEvent;

public interface ICommand<T extends GenericEvent> {
    void execute(final T event);
}
