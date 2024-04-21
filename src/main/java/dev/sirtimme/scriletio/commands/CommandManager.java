package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.commands.admin.AutoDeleteCommand;
import dev.sirtimme.scriletio.commands.admin.RegisterCommand;
import dev.sirtimme.scriletio.commands.owner.UpdateCommand;
import dev.sirtimme.scriletio.commands.user.PingCommand;
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
    }

    public void handleCommand(final SlashCommandInteractionEvent event) {
        final var command = this.commands.get(event.getName());
        command.execute(event);
    }

    public List<CommandData> getCommandData() {
        return this.commands.values().stream().map(ISlashCommand::getCommandData).toList();
    }
}