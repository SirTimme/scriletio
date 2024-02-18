package dev.sirtimme.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {}
}