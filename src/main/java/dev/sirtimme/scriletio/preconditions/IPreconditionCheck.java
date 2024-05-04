package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface IPreconditionCheck {
	boolean check(final SlashCommandInteractionEvent event);
}