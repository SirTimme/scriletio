package dev.sirtimme.scriletio.preconditions.slash;

import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsRegistered implements IPrecondition<SlashCommandInteractionEvent> {
	private final IRepository<User> repository;

	public IsRegistered(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	public boolean isValid(final SlashCommandInteractionEvent event) {
		final var user = repository.get(event.getUser().getIdLong());
		if (user == null) {
			event.reply("You are not registered, please use `/register` first").queue();
			return false;
		}
		return true;
	}
}