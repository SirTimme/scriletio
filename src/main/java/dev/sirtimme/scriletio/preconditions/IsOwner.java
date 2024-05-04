package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsOwner implements IPreconditionCheck {
	@Override
	public boolean check(final SlashCommandInteractionEvent event) {
		if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
			event.reply("This command can only be executed by the owner").setEphemeral(true).queue();
			return false;
		}
		return true;
	}
}