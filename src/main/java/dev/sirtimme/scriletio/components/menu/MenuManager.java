package dev.sirtimme.scriletio.components.menu;

import dev.sirtimme.scriletio.components.menu.delete.DeleteMenu;
import dev.sirtimme.scriletio.components.menu.update.UpdateMenu;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;

public class MenuManager {
	private final HashMap<String, IMenu> menus;

	public MenuManager(final IRepository<User> repository) {
		this.menus = new HashMap<>();
		this.menus.put("update", new UpdateMenu());
		this.menus.put("delete", new DeleteMenu(repository));
	}

	public void handleCommand(final StringSelectInteractionEvent event) {
		final var menuName = event.getComponentId().split(":")[1];
		final var menu = menus.get(menuName);

		menu.execute(event);
	}
}