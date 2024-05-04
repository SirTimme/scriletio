package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.preconditions.IPreconditionCheck;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class PingCommand implements ISlashCommand {
	@Override
	public void execute(final SlashCommandInteractionEvent event) {
		event.getJDA().getRestPing().queue(ping -> event.reply("Received response in **" + ping + "** ms!").queue());
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("ping", "Displays the current latency of Scriletio")
					   .setDescriptionLocalization(DiscordLocale.GERMAN, "Zeigt die aktuelle Latenz von Scriletio an");
	}

	@Override
	public List<IPreconditionCheck> getPreconditions() {
		return List.of();
	}
}