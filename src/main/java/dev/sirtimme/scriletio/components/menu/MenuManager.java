package dev.sirtimme.scriletio.components.menu;

import dev.sirtimme.scriletio.components.menu.delete.DeleteMenu;
import dev.sirtimme.scriletio.components.menu.update.UpdateMenu;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;

public class MenuManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, IMenu> menus;

	public MenuManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.menus = new HashMap<>();
		this.menus.put("update", new UpdateMenu());
		this.menus.put("delete", new DeleteMenu());
	}

	public void handleCommand(final StringSelectInteractionEvent event) {
		final var menuName = event.getComponentId().split(":")[1];
		final var menu = menus.get(menuName);
		final var entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		menu.execute(event, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}