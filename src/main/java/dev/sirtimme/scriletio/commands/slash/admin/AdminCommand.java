package dev.sirtimme.scriletio.commands.slash.admin;

import dev.sirtimme.scriletio.commands.slash.ISlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class AdminCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) {
            event.reply("Admin commands can only be executed within a guild!").queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.reply("You're missing the MANAGE_SERVER permission to execute admin commands!").queue();
            return;
        }

        handleCommand(event);
    }

    protected abstract void handleCommand(final SlashCommandInteractionEvent event);
}