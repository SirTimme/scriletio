package dev.sirtimme.scriletio.preconditions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface IPrecondition {
	PreconditionResult check(final SlashCommandInteractionEvent event);
}