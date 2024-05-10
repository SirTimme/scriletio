package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.button.RegisterAcceptButton;
import dev.sirtimme.scriletio.commands.button.RegisterCancelButton;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ButtonCommandManager {
	private final HashMap<String, Function<EntityManager, ICommand<ButtonInteractionEvent>>> buttonCommands;
	private final EntityManagerFactory entityManagerFactory;

	public ButtonCommandManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;

		this.buttonCommands = new HashMap<>();
		this.buttonCommands.put("registerAccept", entityManager -> new RegisterAcceptButton(new UserRepository(entityManager)));
		this.buttonCommands.put("registerCancel", entityManager -> new RegisterCancelButton());
	}

	public void handleCommand(final ButtonInteractionEvent event) {
		final var commandName = event.getComponentId().split(":")[1];

		try (final var context = entityManagerFactory.createEntityManager()) {
			final var buttonCommand = buttonCommands.get(commandName).apply(context);

			if (buttonCommand.hasInvalidPreconditions(event)) {
				return;
			}

			context.getTransaction().begin();
			buttonCommand.execute(event);
			context.getTransaction().commit();
		}
	}
}