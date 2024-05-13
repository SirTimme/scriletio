package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.events.EventHandler;
import dev.sirtimme.scriletio.managers.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		final var eventHandler = buildEventhandler();

		JDABuilder.createLight(System.getenv("TOKEN"), GatewayIntent.GUILD_MESSAGES)
				  .addEventListeners(eventHandler)
				  .setActivity(Activity.playing("Silentium"))
				  .build();
	}

	private static EventHandler buildEventhandler() {
		final var entityManagerFactory = buildEntityManagerFactory();
		final var deleteJobManager = new DeleteJobManager();

		final var commandManager = new SlashCommandManager(entityManagerFactory);
		final var buttonManager = new ButtonCommandManager(entityManagerFactory);
		final var messageReceiveManager = new MessageReceiveManager(entityManagerFactory, deleteJobManager);
		final var messageDeleteManager = new MessageDeleteManager(entityManagerFactory, deleteJobManager);
		final var menuManager = new MenuCommandManager(entityManagerFactory);
		final var modalManager = new ModalCommandManager(entityManagerFactory);

		return new EventHandler(
				commandManager,
				buttonManager,
				messageReceiveManager,
				messageDeleteManager,
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