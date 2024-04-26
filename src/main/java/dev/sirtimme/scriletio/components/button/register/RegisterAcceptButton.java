package dev.sirtimme.scriletio.components.button.register;

import dev.sirtimme.scriletio.components.button.Button;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.ArrayList;
import java.util.Collections;

public class RegisterAcceptButton extends Button {
	@Override
	protected void handleCommand(final ButtonInteractionEvent event, final EntityManager entityManager) {
		final var repository = new UserRepository(entityManager);

		final var userId = event.getUser().getIdLong();
		final var user = new User(userId, new ArrayList<>());

		repository.add(user);

		event.editMessage("You were successfully registered").setComponents(Collections.emptyList()).queue();
	}
}