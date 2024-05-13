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

public class ButtonCommandManager extends ContextManager<ButtonInteractionEvent> {
	private final HashMap<String, Function<EntityManager, ICommand<ButtonInteractionEvent>>> buttonCommands;

	public ButtonCommandManager(final EntityManagerFactory entityManagerFactory) {
		super(entityManagerFactory);

		this.buttonCommands = new HashMap<>();
		this.buttonCommands.put("registerAccept", entityManager -> new RegisterAcceptButton(new UserRepository(entityManager)));
		this.buttonCommands.put("registerCancel", entityManager -> new RegisterCancelButton());
	}

	@Override
	protected ICommand<ButtonInteractionEvent> getCommand(final ButtonInteractionEvent event, final EntityManager context) {
		final var commandName = event.getComponentId().split(":")[1];
		return buttonCommands.get(commandName).apply(context);
	}
}