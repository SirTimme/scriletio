package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static dev.sirtimme.scriletio.response.Markdown.bold;

public class HasPermission implements IPrecondition<SlashCommandInteractionEvent> {
    private final Permission permission;
    private final LocalizationManager localizationManager;

    public HasPermission(final Permission permission, final LocalizationManager localizationManager) {
        this.permission = permission;
        this.localizationManager = localizationManager;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (!event.getMember().hasPermission(permission)) {
            event.reply(localizationManager.get("precondition.hasPermission.invalid", bold(permission.getName()))).queue();
            return false;
        }

        return true;
    }
}
