package dev.sirtimme.scriletio.commands.menu;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.menu.delete.DeleteMenu;
import dev.sirtimme.scriletio.commands.menu.update.UpdateMenu;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class MenuManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, ICommand<StringSelectInteractionEvent>>> menus;

	public MenuManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.menus = new HashMap<>();
		this.menus.put("update", entityManager -> new UpdateMenu());
		this.menus.put("delete", entityManager -> new DeleteMenu(new UserRepository(entityManager)));
	}

	public void handleCommand(final StringSelectInteractionEvent event) {
		final var menuName = event.getComponentId().split(":")[1];
		final var function = menus.get(menuName);
		final var entityManager = entityManagerFactory.createEntityManager();
		final var menu = function.apply(entityManager);

		if (!menu.checkPreconditions(event)) {
			return;
		}

		entityManager.getTransaction().begin();
		menu.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}