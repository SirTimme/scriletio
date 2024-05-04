package dev.sirtimme.scriletio.preconditions;

import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsRegistered implements IPrecondition {
	final IRepository<User> repository;

	public IsRegistered(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	public PreconditionResult check(final SlashCommandInteractionEvent event) {
		final var user = repository.get(event.getUser().getIdLong());
		if (user == null) {
			event.reply("You are not registered, please use `/register` first").queue();
			return PreconditionResult.FAILURE;
		}

		return PreconditionResult.SUCCESS;
	}
}