package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.commands.message.MessageManager;
import dev.sirtimme.scriletio.commands.slash.CommandManager;
import dev.sirtimme.scriletio.components.button.ButtonManager;
import dev.sirtimme.scriletio.components.menu.MenuManager;
import dev.sirtimme.scriletio.components.modal.ModalManager;
import dev.sirtimme.scriletio.concurrent.DeleteJobManager;
import dev.sirtimme.scriletio.events.EventHandler;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		final var properties = new HashMap<String, String>() {{
			put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
			put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
			put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
		}};
		final var entityManagerFactory = Persistence.createEntityManagerFactory("scriletio", properties);
		final var eventHandler = buildEventhandler(entityManagerFactory);

		JDABuilder.createLight(System.getenv("TOKEN"))
				  .addEventListeners(eventHandler)
				  .setActivity(Activity.playing("Silentium"))
				  .build();
	}

	private static EventHandler buildEventhandler(EntityManagerFactory entityManagerFactory) {
		final var deleteJobManager = new DeleteJobManager();

		final var commandManager = new CommandManager(entityManagerFactory);
		final var buttonManager = new ButtonManager(entityManagerFactory);
		final var messageManager = new MessageManager(entityManagerFactory, deleteJobManager);
		final var menuManager = new MenuManager(entityManagerFactory);
		final var modalManager = new ModalManager(entityManagerFactory);

		return new EventHandler(
				commandManager,
				buttonManager,
				messageManager,
				menuManager,
				modalManager
		);
	}
}