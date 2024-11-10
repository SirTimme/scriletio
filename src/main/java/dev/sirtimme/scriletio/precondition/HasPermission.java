package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HasPermission implements IPrecondition<SlashCommandInteractionEvent> {
    private final Permission permission;
    private final LocalizationManager l10nManager;

    public HasPermission(final Permission permission, final LocalizationManager l10nManager) {
        this.permission = permission;
        this.l10nManager = l10nManager;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (!event.getMember().hasPermission(permission)) {
            event.reply(l10nManager.get("precondition.hasPermission.invalid", permission.getName())).queue();
            return false;
        }

        return true;
    }
}
