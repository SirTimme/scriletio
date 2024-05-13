package dev.sirtimme.scriletio.events;

import dev.sirtimme.scriletio.managers.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
	private final SlashCommandManager slashCommandManager;
	private final ButtonCommandManager buttonCommandManager;
	private final MessageReceiveManager messageReceiveManager;
	private final MessageDeleteManager messageDeleteManager;
	private final MenuCommandManager menuCommandManager;
	private final ModalCommandManager modalCommandManager;

	public EventHandler(
			final SlashCommandManager slashCommandManager,
			final ButtonCommandManager buttonCommandManager,
			final MessageReceiveManager messageReceiveManager,
			final MessageDeleteManager messageDeleteManager,
			final MenuCommandManager menuCommandManager,
			final ModalCommandManager modalCommandManager
	) {
		this.slashCommandManager = slashCommandManager;
		this.buttonCommandManager = buttonCommandManager;
		this.messageReceiveManager = messageReceiveManager;
		this.messageDeleteManager = messageDeleteManager;
		this.menuCommandManager = menuCommandManager;
		this.modalCommandManager = modalCommandManager;
	}

	@Override
	public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
		slashCommandManager.handleCommand(event);
	}

	@Override
	public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
		buttonCommandManager.handleCommand(event);
	}

	@Override
	public void onModalInteraction(@NotNull final ModalInteractionEvent event) {
		modalCommandManager.handleCommand(event);
	}

	@Override
	public void onStringSelectInteraction(@NotNull final StringSelectInteractionEvent event) {
		menuCommandManager.handleCommand(event);
	}

	@Override
	public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
		messageReceiveManager.handleCommand(event);
	}

	@Override
	public void onMessageDelete(@NotNull final MessageDeleteEvent event) {
		messageDeleteManager.handleCommand(event);
	}
}