package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HasSavedConfigs implements IPrecondition<SlashCommandInteractionEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HasSavedConfigs.class);
    private final QueryableRepository<DeleteConfig> configRepository;
    private final LocalizationManager l10nManager;

    public HasSavedConfigs(final QueryableRepository<DeleteConfig> configRepository, final LocalizationManager l10nManager) {
        this.configRepository = configRepository;
        this.l10nManager = l10nManager;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());

        LOGGER.debug("Found '{}' configs for guild with id '{}'", deleteConfigs.size(), event.getGuild().getIdLong());

        if (deleteConfigs.isEmpty()) {
            event.reply(l10nManager.get("precondition.hasSavedConfigs.invalid")).queue();
            return false;
        }

        return true;
    }
}