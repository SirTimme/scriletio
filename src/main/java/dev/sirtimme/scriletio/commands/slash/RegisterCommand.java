package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.IsNotRegistered;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class RegisterCommand implements ISlashCommand {
	private final IRepository<User> repository;

	public RegisterCommand(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var btnAccept = Button.success(userId + ":registerAccept", "Accept");
		final var btnCancel = Button.danger(userId + ":registerCancel", "Cancel");

		event.reply(Formatter.format()).addActionRow(btnAccept, btnCancel).queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("register", "Register yourself to use Scriletios services");
	}

	@Override
	public List<IPrecondition> getPreconditions() {
		return List.of(
				new IsNotRegistered(repository)
		);
	}
}