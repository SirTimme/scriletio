package dev.sirtimme.scriletio.components.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public abstract class Modal implements IModal {
	@Override
	public void execute(final ModalInteractionEvent event) {
		final var authorId = event.getModalId().split(":")[0];
		if (!event.getUser().getId().equals(authorId)) {
			event.reply("You are not the author of the initial message").setEphemeral(true).queue();
			return;
		}

		handleCommand(event);
	}

	protected abstract void handleCommand(final ModalInteractionEvent event);
}