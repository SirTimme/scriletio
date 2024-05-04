package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.PreconditionResult;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface ISlashCommand {
	void execute(final SlashCommandInteractionEvent event);

	CommandData getCommandData();

	List<IPrecondition> getPreconditions();

	default PreconditionResult checkPreconditions(final SlashCommandInteractionEvent event) {
		for (final var precondition : getPreconditions()) {
			if (precondition.check(event) == PreconditionResult.FAILURE) {
				return PreconditionResult.FAILURE;
			}
		}

		return PreconditionResult.SUCCESS;
	}
}