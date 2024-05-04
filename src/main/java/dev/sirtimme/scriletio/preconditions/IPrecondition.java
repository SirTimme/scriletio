package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface IPrecondition {
	boolean check(final SlashCommandInteractionEvent event);
}