package dev.sirtimme.scriletio.precondition;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

import java.util.List;

public interface IPreconditionHolder<T extends GenericInteractionCreateEvent> {
    default boolean hasInvalidPreconditions(final T event) {
        for (final var precondition : getPreconditions()) {
            if (!precondition.isValid(event)) {
                return true;
            }
        }
        return false;
    }

    List<IPrecondition<T>> getPreconditions();
}
