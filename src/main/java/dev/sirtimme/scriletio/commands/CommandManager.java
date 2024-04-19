package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.commands.admin.AutoDeleteCommand;
import dev.sirtimme.scriletio.commands.owner.UpdateCommand;
import dev.sirtimme.scriletio.commands.user.PingCommand;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
    private final HashMap<String, ISlashCommand> commands;

    public CommandManager() {
        final var properties = new HashMap<String, String>() {{
            put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
            put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
            put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
        }};
        final var entityManagerFactory = Persistence.createEntityManagerFactory("scriletio", properties);
        final var repository = new UserRepository(entityManagerFactory);

        this.commands = new HashMap<>();
        this.commands.put("ping", new PingCommand());
        this.commands.put("update", new UpdateCommand(this));
        this.commands.put("autodelete", new AutoDeleteCommand(repository));
    }

    public void handleCommand(final SlashCommandInteractionEvent event) {
        final var command = this.commands.get(event.getName());
        command.execute(event);
    }

    public List<CommandData> getCommandData() {
        return this.commands.values().stream().map(ISlashCommand::getCommandData).toList();
    }
}