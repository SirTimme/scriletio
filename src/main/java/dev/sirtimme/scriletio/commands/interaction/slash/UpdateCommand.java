package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.scriletio.factory.interaction.SlashEventCommandFactory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isOwner;
import static dev.sirtimme.scriletio.response.Markdown.bold;

public class UpdateCommand implements ISlashCommand {
    private final SlashEventCommandFactory slashCommandManager;
    private final LocalizationManager localizationManager;

    public UpdateCommand(final SlashEventCommandFactory slashCommandManager, final LocalizationManager localizationManager) {
        this.slashCommandManager = slashCommandManager;
        this.localizationManager = localizationManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var localizationFunc = ResourceBundleLocalizationFunction
            .fromBundles("localization/commands", DiscordLocale.ENGLISH_US, DiscordLocale.GERMAN)
            .build();

        final var commandData = slashCommandManager
            .getCommandData()
            .stream()
            .map(cmdData -> cmdData.setLocalizationFunction(localizationFunc))
            .toList();

        event.getJDA()
             .updateCommands()
             .addCommands(commandData)
             .queue();

        event.reply(localizationManager.get("slash.update", bold(commandData.size()))).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            isOwner(localizationManager)
        );
    }

    @Override
    public CommandData getCommandData() {
        final var commandName = localizationManager.get("update.name", Locale.US);
        final var commandDescription = localizationManager.get("update.description", Locale.US);

        return Commands.slash(commandName, commandDescription).setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}