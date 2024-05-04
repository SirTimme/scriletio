package dev.sirtimme.scriletio.events;

import dev.sirtimme.scriletio.commands.CommandManager;
import dev.sirtimme.scriletio.commands.MessageManager;
import dev.sirtimme.scriletio.components.button.ButtonManager;
import dev.sirtimme.scriletio.components.menu.MenuManager;
import dev.sirtimme.scriletio.components.modal.ModalManager;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
	private final CommandManager commandManager;
	private final ButtonManager buttonManager;
	private final MessageManager messageManager;
	private final MenuManager menuManager;
	private final ModalManager modalManager;

	public EventHandler(
			final CommandManager commandManager,
			final ButtonManager buttonManager,
			final MessageManager messageManager,
			final MenuManager menuManager,
			final ModalManager modalManager
	) {
		this.commandManager = commandManager;
		this.buttonManager = buttonManager;
		this.messageManager = messageManager;
		this.menuManager = menuManager;
		this.modalManager = modalManager;
	}

	@Override
	public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
		commandManager.handleCommand(event);
	}

	@Override
	public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
		buttonManager.handleCommand(event);
	}

	@Override
	public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
		messageManager.handleMessageReceive(event);
	}

	@Override
	public void onMessageDelete(@NotNull final MessageDeleteEvent event) {
		messageManager.handleMessageDelete(event);
	}

	@Override
	public void onModalInteraction(@NotNull final ModalInteractionEvent event) {
		modalManager.handleCommand(event);
	}

	@Override
	public void onStringSelectInteraction(@NotNull final StringSelectInteractionEvent event) {
		menuManager.handleCommand(event);
	}
}