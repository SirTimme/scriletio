package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.precondition.IPrecondition;
import dev.sirtimme.iuvo.repository.QueryableRepository;
import dev.sirtimme.iuvo.repository.Repository;
import dev.sirtimme.scriletio.commands.interaction.sub.AddConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.DeleteConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.GetConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.UpdateConfigCommand;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class AutoDeleteCommand implements ISlashCommand {
    private final HashMap<DeleteSubCommand, Supplier<ISubCommand>> subCommands;

    public AutoDeleteCommand(final Repository<User> userRepository, final QueryableRepository<DeleteConfig> configRepository) {
        this.subCommands = new HashMap<>();
        this.subCommands.put(DeleteSubCommand.ADD, () -> new AddConfigCommand(configRepository, userRepository));
        this.subCommands.put(DeleteSubCommand.GET, () -> new GetConfigCommand(configRepository));
        this.subCommands.put(DeleteSubCommand.UPDATE, () -> new UpdateConfigCommand(configRepository, userRepository));
        this.subCommands.put(DeleteSubCommand.DELETE, () -> new DeleteConfigCommand(configRepository, userRepository));
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.reply("You're missing the MANAGE_SERVER permission to execute admin commands!").queue();
            return;
        }

        // noinspection DataFlowIssue this command only consists of subcommands
        final var subCommandName = DeleteSubCommand.valueOf(event.getSubcommandName().toUpperCase());
        final var subCommand = subCommands.get(subCommandName).get();

        if (subCommand.hasInvalidPreconditions(event)) {
            return;
        }

        subCommand.execute(event, locale);
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of();
    }

    @Override
    public CommandData getCommandData() {
        final var subCommandData = subCommands
            .values()
            .stream()
            .map(function -> function.get().getSubCommandData())
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