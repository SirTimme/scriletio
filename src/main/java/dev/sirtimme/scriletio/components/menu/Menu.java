package dev.sirtimme.scriletio.components.menu;

import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public abstract class Menu implements IMenu {
	@Override
	public void execute(final StringSelectInteractionEvent event, final EntityManager entityManager) {
		final var authorId = event.getComponentId().split(":")[0];
		if (!event.getUser().getId().equals(authorId)) {
			event.reply("You are not the author of the initial message").setEphemeral(true).queue();
			return;
		}

		handleCommand(event, entityManager);
	}

	protected abstract void handleCommand(final StringSelectInteractionEvent event, final EntityManager entityManager);
}