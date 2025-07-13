package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.commands.interaction.sub.AddConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.DeleteConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.GetConfigCommand;
import dev.sirtimme.scriletio.commands.interaction.sub.UpdateConfigCommand;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.precondition.HasPermission;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class AutoDeleteCommand implements ISlashCommand {
    private final HashMap<String, Supplier<ISubCommand>> subCommands;
    private final LocalizationManager localizationManager;

    public AutoDeleteCommand(
        final Repository<User> userRepository,
        final QueryableRepository<DeleteConfig> configRepository,
        final LocalizationManager localizationManager
    ) {
        this.subCommands = new HashMap<>();
        this.localizationManager = localizationManager;

        registerSubcommand("auto-delete.add.name", () -> new AddConfigCommand(configRepository, userRepository, localizationManager));
        registerSubcommand("auto-delete.get.name", () -> new GetConfigCommand(configRepository, localizationManager));
        registerSubcommand("auto-delete.update.name", () -> new UpdateConfigCommand(configRepository, userRepository, localizationManager));
        registerSubcommand("auto-delete.delete.name", () -> new DeleteConfigCommand(configRepository, userRepository, localizationManager));
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var subCommand = subCommands.get(event.getSubcommandName()).get();

        if (subCommand.hasInvalidPreconditions(event)) {
            return;
        }

        subCommand.execute(event);
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasPermission(Permission.MANAGE_SERVER, localizationManager)
        );
    }

    @Override
    public CommandData getCommandData() {
        final var subCommandData = subCommands
            .values()
            .stream()
            .map(function -> function.get().getSubCommandData())
            .toList();

        final var commandName = localizationManager.get("auto-delete.name", Locale.US);
        final var commandDescription = localizationManager.get("auto-delete.description", Locale.US);

        return Commands.slash(commandName, commandDescription).addSubcommands(subCommandData).setContexts(InteractionContextType.GUILD);
    }

    private void registerSubcommand(final String key, final Supplier<ISubCommand> value) {
        subCommands.put(localizationManager.get(key, Locale.US), value);
    }
}