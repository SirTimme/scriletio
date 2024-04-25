package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.slash.admin.AutoDeleteCommand;
import dev.sirtimme.scriletio.commands.slash.admin.RegisterCommand;
import dev.sirtimme.scriletio.commands.slash.owner.UpdateCommand;
import dev.sirtimme.scriletio.commands.slash.user.DeleteCommand;
import dev.sirtimme.scriletio.commands.slash.user.PingCommand;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
	private final HashMap<String, ISlashCommand> commands;

	public CommandManager(final IRepository<User> repository) {
		this.commands = new HashMap<>();
		this.commands.put("ping", new PingCommand());
		this.commands.put("update", new UpdateCommand(this));
		this.commands.put("autodelete", new AutoDeleteCommand(repository));
		this.commands.put("register", new RegisterCommand(repository));
		this.commands.put("delete", new DeleteCommand(repository));
	}

	public void handleCommand(final SlashCommandInteractionEvent event) {
		final var command = this.commands.get(event.getName());
		command.execute(event);
	}

	public List<CommandData> getCommandData() {
		return this.commands.values().stream().map(ISlashCommand::getCommandData).toList();
	}
}