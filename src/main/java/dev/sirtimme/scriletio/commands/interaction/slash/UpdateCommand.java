package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.scriletio.factory.interaction.SlashEventCommandFactory;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isOwner;

public class UpdateCommand implements ISlashCommand {
    private final SlashEventCommandFactory slashCommandManager;
    private final LocalizationManager l10nManager;

    public UpdateCommand(final SlashEventCommandFactory slashCommandManager, final LocalizationManager l10nManager) {
        this.slashCommandManager = slashCommandManager;
        this.l10nManager = l10nManager;
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

        event.reply("Update of slash commands were successful!").queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            isOwner()
        );
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(l10nManager.get("update.name", Locale.US), l10nManager.get("update.description", Locale.US))
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}