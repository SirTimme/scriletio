package dev.sirtimme.scriletio.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface ISubCommand extends ICommand<SlashCommandInteractionEvent> {
    SubcommandData getSubcommandData();
}
