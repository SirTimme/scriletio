package dev.sirtimme.scriletio.commands.slash.admin;

import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RegisterCommand extends AdminCommand {
	private final IRepository<User> repository;

	public RegisterCommand(final IRepository<User> repository) {
		this.repository = repository;
	}

	@Override
	protected void handleCommand(final SlashCommandInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var user = repository.get(userId);
		if (user != null) {
			event.reply("You are already registered, no need to use that command again").queue();
			return;
		}

		final var btnAccept = Button.success(userId + ":registerAccept", "Accept");
		final var btnCancel = Button.danger(userId + ":registerCancel", "Cancel");

		event.reply(Formatter.format()).addActionRow(btnAccept, btnCancel).queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("register", "Register yourself to use Scriletios services");
	}
}