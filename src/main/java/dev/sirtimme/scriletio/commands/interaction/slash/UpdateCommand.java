package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.scriletio.factory.interaction.SlashEventCommandFactory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;

import java.util.List;

public class UpdateCommand implements ISlashCommand {
    private final SlashEventCommandFactory manager;

    public UpdateCommand(final SlashEventCommandFactory manager) {
        this.manager = manager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
            event.reply("This command can only be executed by the owner").setEphemeral(true).queue();
            return;
        }

        final var localizationFunc = ResourceBundleLocalizationFunction
            .fromBundles("localization/commands", DiscordLocale.ENGLISH_US, DiscordLocale.GERMAN)
            .build();

        final var commandData = manager
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
        return List.of();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("update", "Refreshes all slash commands")
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}