package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.menu.DeleteMenu;
import dev.sirtimme.scriletio.commands.menu.UpdateMenu;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class MenuCommandManager extends ContextManager<StringSelectInteractionEvent> {
	private final HashMap<String, Function<EntityManager, ICommand<StringSelectInteractionEvent>>> menuCommands;

	public MenuCommandManager(final EntityManagerFactory entityManagerFactory) {
		super(entityManagerFactory);

		this.menuCommands = new HashMap<>();
		this.menuCommands.put("update", entityManager -> new UpdateMenu());
		this.menuCommands.put("delete", entityManager -> new DeleteMenu(new UserRepository(entityManager)));
	}

	@Override
	protected ICommand<StringSelectInteractionEvent> getCommand(final StringSelectInteractionEvent event, final EntityManager context) {
		final var commandName = event.getComponentId().split(":")[1];
		return menuCommands.get(commandName).apply(context);
	}
}