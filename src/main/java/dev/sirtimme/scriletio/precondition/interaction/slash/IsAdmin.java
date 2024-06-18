package dev.sirtimme.scriletio.precondition.interaction.slash;

import dev.sirtimme.scriletio.precondition.IPrecondition;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsAdmin implements IPrecondition<SlashCommandInteractionEvent> {
    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // command can only be executed within a guild
        // noinspection DataFlowIssue
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.reply("You're missing the MANAGE_SERVER permission to execute admin commands!").queue();
            return false;
        }

        return true;
    }
}