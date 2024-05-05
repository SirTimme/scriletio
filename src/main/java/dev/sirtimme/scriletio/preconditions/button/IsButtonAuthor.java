package dev.sirtimme.scriletio.preconditions.button;

import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.PreconditionResult;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class IsButtonAuthor implements IPrecondition<ButtonInteractionEvent> {
	@Override
	public PreconditionResult check(final ButtonInteractionEvent event) {
		final var authorId = event.getComponentId().split(":")[0];
		if (!event.getUser().getId().equals(authorId)) {
			event.reply("You are not the author of the initial message").setEphemeral(true).queue();
			return PreconditionResult.FAILURE;
		}

		return PreconditionResult.SUCCESS;
	}
}