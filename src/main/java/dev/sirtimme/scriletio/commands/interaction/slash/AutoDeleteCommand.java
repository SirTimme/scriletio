package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.scriletio.commands.interaction.sub.ISubCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.AddConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.DeleteConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.GetConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.UpdateConfigCommand;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.slash.IsAdmin;
import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class AutoDeleteCommand implements ISlashCommand {
    private final HashMap<DeleteSubCommand, Supplier<ISubCommand>> subCommands;

    public AutoDeleteCommand(final IRepository<User> userRepository, final IQueryableRepository<DeleteConfig> deleteConfigRepository) {
        this.subCommands = new HashMap<>();
        this.subCommands.put(DeleteSubCommand.ADD, () -> new AddConfigCommand(deleteConfigRepository, userRepository));
        this.subCommands.put(DeleteSubCommand.GET, () -> new GetConfigCommand(deleteConfigRepository));
        this.subCommands.put(DeleteSubCommand.UPDATE, () -> new UpdateConfigCommand(deleteConfigRepository));
        this.subCommands.put(DeleteSubCommand.DELETE, () -> new DeleteConfigCommand(deleteConfigRepository));
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // this command only consists of subcommands
        final var subCommandName = DeleteSubCommand.valueOf(event.getSubcommandName().toUpperCase());
        final var subCommand = subCommands.get(subCommandName).get();

        if (subCommand.hasInvalidPreconditions(event)) {
            return;
        }

        subCommand.execute(event);
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new IsAdmin()
        );
    }

    @Override
    public CommandData getCommandData() {
        final var subCommandData = subCommands
            .values()
            .stream()
            .map(function -> function.get().getSubcommandData())
            .toList();

        return Commands.slash("autodelete", "Manage auto delete configs")
                       .addSubcommands(subCommandData)
                       .setGuildOnly(true);
    }

    private enum DeleteSubCommand {
        ADD,
        GET,
        UPDATE,
        DELETE,
    }
}