package dev.sirtimme.scriletio.precondition.component;

import dev.sirtimme.iuvo.precondition.IPrecondition;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public class IsComponentAuthor implements IPrecondition<GenericComponentInteractionCreateEvent> {
    @Override
    public boolean isValid(final GenericComponentInteractionCreateEvent event) {
        final var authorId = event.getComponentId().split(":")[0];

        if (!event.getUser().getId().equals(authorId)) {
            event.reply("You are not the author of the initial message").setEphemeral(true).queue();
            return false;
        }

        return true;
    }
}
