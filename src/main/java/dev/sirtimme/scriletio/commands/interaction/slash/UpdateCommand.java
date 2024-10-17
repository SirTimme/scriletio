package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.precondition.IPrecondition;
import dev.sirtimme.scriletio.factory.interaction.SlashEventCommandFactory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.Locale;

public class UpdateCommand implements ISlashCommand {
    private final SlashEventCommandFactory manager;

    public UpdateCommand(final SlashEventCommandFactory manager) {
        this.manager = manager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
            event.reply("This command can only be executed by the owner").setEphemeral(true).queue();
            return;
        }

        event.getJDA()
             .updateCommands()
             .addCommands(manager.getCommandData())
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
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Aktualisiert alle Befehle")
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}