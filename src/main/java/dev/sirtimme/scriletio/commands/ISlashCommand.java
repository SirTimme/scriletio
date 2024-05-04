package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.preconditions.IPreconditionCheck;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface ISlashCommand {
	void execute(final SlashCommandInteractionEvent event);

	CommandData getCommandData();

	List<IPreconditionCheck> getPreconditions();

	default boolean verifyPreconditions(final SlashCommandInteractionEvent event) {
		for (var precondition : getPreconditions()) {
			if (!precondition.check(event)) {
				return false;
			}
		}
		return true;
	}
}