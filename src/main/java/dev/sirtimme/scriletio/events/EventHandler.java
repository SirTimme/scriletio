package dev.sirtimme.scriletio.events;

import dev.sirtimme.scriletio.commands.message.MessageManager;
import dev.sirtimme.scriletio.commands.slash.CommandManager;
import dev.sirtimme.scriletio.components.button.ButtonManager;
import dev.sirtimme.scriletio.components.menu.MenuManager;
import dev.sirtimme.scriletio.components.modal.ModalManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EventHandler extends ListenerAdapter {
	private final CommandManager commandManager;
	private final ButtonManager buttonManager;
	private final MessageManager messageManager;
	private final MenuManager menuManager;
	private final ModalManager modalManager;

	public EventHandler() {
		final var properties = new HashMap<String, String>() {{
			put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
			put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
			put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
		}};
		final var entityManagerFactory = Persistence.createEntityManagerFactory("scriletio", properties);
		final var userRepository = new UserRepository(entityManagerFactory);
		final var deleteConfigRepository = new DeleteConfigRepository(entityManagerFactory);

		this.commandManager = new CommandManager(userRepository);
		this.buttonManager = new ButtonManager(userRepository);
		this.messageManager = new MessageManager(deleteConfigRepository);
		this.menuManager = new MenuManager(userRepository);
		this.modalManager = new ModalManager(deleteConfigRepository);
	}

	@Override
	public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
		this.commandManager.handleCommand(event);
	}

	@Override
	public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
		this.buttonManager.handleCommand(event);
	}

	@Override
	public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
		this.messageManager.handleMessageReceive(event);
	}

	@Override
	public void onMessageDelete(@NotNull final MessageDeleteEvent event) {
		this.messageManager.handleMessageDelete(event);
	}

	@Override
	public void onModalInteraction(@NotNull final ModalInteractionEvent event) {
		this.modalManager.handleCommand(event);
	}

	@Override
	public void onStringSelectInteraction(@NotNull final StringSelectInteractionEvent event) {
		this.menuManager.handleCommand(event);
	}
}