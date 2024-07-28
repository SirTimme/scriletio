package dev.sirtimme.scriletio.commands.listener;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventListenerBase<T extends GenericEvent> implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerBase.class);
    private final Class<T> clazz;

    protected EventListenerBase(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onEvent(@NotNull final GenericEvent genericEvent) {
        if (clazz.isInstance(genericEvent)) {
            LOGGER.info("Received event with type {}", clazz.getSimpleName());

            handleCommand(clazz.cast(genericEvent));
        }
    }

    abstract void handleCommand(final T event);
}