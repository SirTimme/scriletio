package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
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

import static dev.sirtimme.scriletio.localization.LocalizationManager.getResponse;

public class AutoDeleteCommand implements ISlashCommand {
    private final HashMap<String, Supplier<ISubCommand>> subCommands;

    public AutoDeleteCommand(final Repository<User> userRepository, final QueryableRepository<DeleteConfig> configRepository) {
        subCommands = new HashMap<>();
        subCommands.put(getResponse("auto-delete.add.name", Locale.US), () -> new AddConfigCommand(configRepository, userRepository));
        subCommands.put(getResponse("auto-delete.get.name", Locale.US), () -> new GetConfigCommand(configRepository));
        subCommands.put(getResponse("auto-delete.update.name", Locale.US), () -> new UpdateConfigCommand(configRepository, userRepository));
        subCommands.put(getResponse("auto-delete.delete.name", Locale.US), () -> new DeleteConfigCommand(configRepository, userRepository));
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.reply("You're missing the MANAGE_SERVER permission to execute admin commands!").queue();
            return;
        }

        final var subCommand = subCommands.get(event.getSubcommandName()).get();

        if (subCommand.hasInvalidPreconditions(event)) {
            return;
        }

        subCommand.execute(event);
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

        return Commands.slash(getResponse("auto-delete.name", Locale.US), getResponse("auto-delete.description", Locale.US))
                       .addSubcommands(subCommandData)
                       .setGuildOnly(true);
    }
}