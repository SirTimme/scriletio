package dev.sirtimme.scriletio.commands.slash.owner;

import dev.sirtimme.scriletio.commands.slash.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class OwnerCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
            event.reply("This command can only be executed by the owner").setEphemeral(true).queue();
            return;
        }

        handleCommand(event);
    }

    protected abstract void handleCommand(final SlashCommandInteractionEvent event);
}