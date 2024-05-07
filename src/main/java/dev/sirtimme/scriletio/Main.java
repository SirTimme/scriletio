package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.managers.ButtonManager;
import dev.sirtimme.scriletio.managers.MenuManager;
import dev.sirtimme.scriletio.managers.MessageManager;
import dev.sirtimme.scriletio.managers.ModalManager;
import dev.sirtimme.scriletio.managers.CommandManager;
import dev.sirtimme.scriletio.events.EventHandler;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		final var eventHandler = buildEventhandler();

		JDABuilder.createLight(System.getenv("TOKEN"))
				  .addEventListeners(eventHandler)
				  .setActivity(Activity.playing("Silentium"))
				  .build();
	}

	private static EventHandler buildEventhandler() {
		final var entityManagerFactory = buildEntityManagerFactory();

		final var commandManager = new CommandManager(entityManagerFactory);
		final var buttonManager = new ButtonManager(entityManagerFactory);
		final var messageManager = new MessageManager(entityManagerFactory);
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

	private static EntityManagerFactory buildEntityManagerFactory() {
		final var properties = new HashMap<String, String>() {{
			put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
			put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
			put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
		}};

		return Persistence.createEntityManagerFactory("scriletio", properties);
	}
}