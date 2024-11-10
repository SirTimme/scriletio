package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static dev.sirtimme.scriletio.localization.LocalizationManager.getResponse;

public class HasPermission implements IPrecondition<SlashCommandInteractionEvent> {
    private final Permission permission;

    public HasPermission(final Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (!event.getMember().hasPermission(permission)) {
            event.reply(getResponse("precondition.hasPermission.invalid", permission.getName())).queue();
            return false;
        }

        return true;
    }
}
