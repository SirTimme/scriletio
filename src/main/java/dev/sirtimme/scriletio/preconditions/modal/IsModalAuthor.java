package dev.sirtimme.scriletio.preconditions.modal;

import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.PreconditionResult;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class IsModalAuthor implements IPrecondition<ModalInteractionEvent> {
	@Override
	public PreconditionResult check(final ModalInteractionEvent event) {
		final var authorId = event.getModalId().split(":")[0];
		if (!event.getUser().getId().equals(authorId)) {
			event.reply("You are not the author of the initial message").setEphemeral(true).queue();
			return PreconditionResult.FAILURE;
		}

		return PreconditionResult.SUCCESS;
	}
}