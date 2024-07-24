package dev.sirtimme.scriletio.managers;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public abstract class CommandManager<T extends GenericEvent> implements EventListener {
    private final Class<T> clazz;

    protected CommandManager(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onEvent(@NotNull final GenericEvent genericEvent) {
        if (clazz.isInstance(genericEvent)) {
            handleCommand(clazz.cast(genericEvent));
        }
    }

    abstract void handleCommand(final T event);
}