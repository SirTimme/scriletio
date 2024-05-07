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

public class MenuCommandManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, ICommand<StringSelectInteractionEvent>>> menuCommands;

	public MenuCommandManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.menuCommands = new HashMap<>();
		this.menuCommands.put("update", entityManager -> new UpdateMenu());
		this.menuCommands.put("delete", entityManager -> new DeleteMenu(new UserRepository(entityManager)));
	}

	public void handleCommand(final StringSelectInteractionEvent event) {
		final var commandName = event.getComponentId().split(":")[1];
		final var entityManager = entityManagerFactory.createEntityManager();
		final var menuCommand = menuCommands.get(commandName).apply(entityManager);

		if (menuCommand.hasInvalidPreconditions(event)) {
			return;
		}

		entityManager.getTransaction().begin();
		menuCommand.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}