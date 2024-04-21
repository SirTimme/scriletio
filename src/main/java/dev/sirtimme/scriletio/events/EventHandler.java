package dev.sirtimme.scriletio.events;

import dev.sirtimme.scriletio.commands.CommandManager;
import dev.sirtimme.scriletio.components.buttons.ButtonManager;
import dev.sirtimme.scriletio.messages.MessageManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final CommandManager manager;
    private final ButtonManager buttonManager;

    public EventHandler() {
        this.manager = new CommandManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        this.manager.handleCommand(event);
    }

    @Override
    public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
        this.buttonManager.handleCommand(event);
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        this.messageManager.handleMessage(event);
    }
}