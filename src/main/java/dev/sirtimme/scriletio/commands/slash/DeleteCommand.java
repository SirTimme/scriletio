package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.preconditions.HasRegistered;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class DeleteCommand implements ISlashCommand {
	private final IRepository<User> repository;

	public DeleteCommand(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event) {
		final var user = repository.get(event.getUser().getIdLong());

		repository.delete(user);

		event.reply("All of your stored data is gone").queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("delete", "Deletes all of your stored data")
					   .setDescriptionLocalization(DiscordLocale.GERMAN, "Löscht all deine gespeicherten Daten");
	}

	@Override
	public List<IPrecondition> getPreconditions() {
		return List.of(
				new HasRegistered(repository)
		);
	}
}