package dev.sirtimme.scriletio.components.menu.delete;

import dev.sirtimme.scriletio.components.menu.Menu;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;

public class DeleteMenu extends Menu {
	@Override
	protected void handleCommand(final StringSelectInteractionEvent event, final EntityManager entityManager) {
		final var repository = new UserRepository(entityManager);

		final var userId = event.getUser().getIdLong();
		final var user = repository.get(userId);
		final var channelId = event.getValues().getFirst();

		user.removeConfig(Long.parseLong(channelId));
		repository.update(user);

		event.editMessage("Config for channel <#" + channelId + "> was successfully deleted").setComponents(Collections.emptyList()).queue();
	}
}