package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static dev.sirtimme.scriletio.response.Markdown.bold;

public class HasLessThanConfigs implements IPrecondition<SlashCommandInteractionEvent> {
    private final int limit;
    private final QueryableRepository<DeleteConfig> configRepository;
    private final LocalizationManager l10nManager;

    public HasLessThanConfigs(final int limit, final QueryableRepository<DeleteConfig> configRepository, final LocalizationManager l10nManager) {
        this.limit = limit;
        this.configRepository = configRepository;
        this.l10nManager = l10nManager;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (configRepository.findAll(event.getGuild().getIdLong()).size() >= limit) {
            event.reply(l10nManager.get("precondition.hasLessThanConfigs.invalid", bold(limit))).queue();
            return false;
        }

        return true;
    }
}
