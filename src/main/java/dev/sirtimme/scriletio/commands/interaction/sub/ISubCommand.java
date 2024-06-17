package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.scriletio.commands.interaction.IInteractionCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface ISubCommand extends IInteractionCommand<SlashCommandInteractionEvent> {
    SubcommandData getSubcommandData();
}
