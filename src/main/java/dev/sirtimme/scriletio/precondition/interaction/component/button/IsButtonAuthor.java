package dev.sirtimme.scriletio.precondition.interaction.component.button;

import dev.sirtimme.scriletio.precondition.IPrecondition;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class IsButtonAuthor implements IPrecondition<ButtonInteractionEvent> {
    @Override
    public boolean isValid(final ButtonInteractionEvent event) {
        final var authorId = event.getComponentId().split(":")[0];

        if (!event.getUser().getId().equals(authorId)) {
            event.reply("You are not the author of the initial message").setEphemeral(true).queue();
            return false;
        }

        return true;
    }
}