package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsAdmin implements IPreconditionCheck {
	@Override
	public boolean check(final SlashCommandInteractionEvent event) {
		if (event.getGuild() == null) {
			event.reply("Admin commands can only be executed within a guild!").queue();
			return false;
		}

		if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
			event.reply("You're missing the MANAGE_SERVER permission to execute admin commands!").queue();
			return false;
		}

		return true;
	}
}