package dev.sirtimme.scriletio.components.button.register;

import dev.sirtimme.scriletio.components.button.Button;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

public class RegisterAcceptButton extends Button {
	private final IRepository<User> repository;

	public RegisterAcceptButton(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	protected void handleCommand(final ButtonInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var user = new User(userId, List.of());

		repository.add(user);

		event.editMessage("You were successfully registered").setComponents(Collections.emptyList()).queue();
	}
}