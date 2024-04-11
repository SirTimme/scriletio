package dev.sirtimme.scriletio.events;

import dev.sirtimme.scriletio.commands.CommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final CommandManager manager;

    public EventHandler() {
        this.manager = new CommandManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        this.manager.handleCommand(event);
    }
}