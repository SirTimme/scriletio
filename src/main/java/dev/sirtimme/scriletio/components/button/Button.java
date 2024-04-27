package dev.sirtimme.scriletio.components.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class Button implements IButton {
	@Override
	public void execute(final ButtonInteractionEvent event) {
		final var authorId = event.getComponentId().split(":")[0];
		if (!event.getUser().getId().equals(authorId)) {
			event.reply("You are not the author of the initial message").setEphemeral(true).queue();
			return;
		}

		handleCommand(event);
	}

	protected abstract void handleCommand(final ButtonInteractionEvent event);
}