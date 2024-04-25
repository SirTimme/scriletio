package dev.sirtimme.scriletio.commands.slash.user;

import dev.sirtimme.scriletio.commands.slash.ISlashCommand;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class DeleteCommand implements ISlashCommand {
	private final IRepository<User> repository;

	public DeleteCommand(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event) {
		final var userId = event.getUser().getIdLong();

		repository.delete(userId);

		event.reply("All of your stored data is gone").queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("delete", "Deletes all of your stored data")
					   .setDescriptionLocalization(DiscordLocale.GERMAN, "Löscht all deine gespeicherten Daten");
	}
}