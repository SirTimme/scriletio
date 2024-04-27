package dev.sirtimme.scriletio.components.menu.delete;

import dev.sirtimme.scriletio.components.menu.Menu;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;

public class DeleteMenu extends Menu {
	private final IRepository<User> repository;

	public DeleteMenu(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	protected void handleCommand(final StringSelectInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var user = repository.get(userId);
		final var channelId = event.getValues().getFirst();

		user.removeConfig(Long.parseLong(channelId));
		repository.update(user);

		event.editMessage("Config for channel <#" + channelId + "> was successfully deleted").setComponents(Collections.emptyList()).queue();
	}
}