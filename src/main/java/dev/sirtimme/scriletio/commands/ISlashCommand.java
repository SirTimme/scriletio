package dev.sirtimme.scriletio.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ISlashCommand extends ICommand<SlashCommandInteractionEvent> {
	CommandData getCommandData();
}