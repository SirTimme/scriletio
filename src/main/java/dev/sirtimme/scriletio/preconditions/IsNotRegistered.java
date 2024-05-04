package dev.sirtimme.scriletio.preconditions;

import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsNotRegistered implements IPrecondition {
	private final IRepository<User> repository;

	public IsNotRegistered(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	public boolean check(final SlashCommandInteractionEvent event) {
		final var user = repository.get(event.getUser().getIdLong());
		if (user != null) {
			event.reply("You are already registered").queue();
			return false;
		}
		return true;
	}
}