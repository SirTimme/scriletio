package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsOwner implements IPrecondition {
	@Override
	public PreconditionResult check(final SlashCommandInteractionEvent event) {
		if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
			event.reply("This command can only be executed by the owner").setEphemeral(true).queue();
			return PreconditionResult.FAILURE;
		}

		return PreconditionResult.SUCCESS;
	}
}