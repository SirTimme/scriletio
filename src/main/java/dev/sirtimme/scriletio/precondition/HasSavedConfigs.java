package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.sirtimme.scriletio.localization.LocalizationManager.getResponse;

public class HasSavedConfigs implements IPrecondition<SlashCommandInteractionEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HasSavedConfigs.class);
    private final QueryableRepository<DeleteConfig> configRepository;

    public HasSavedConfigs(final QueryableRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());

        LOGGER.debug("Found {} configs for guild with id {}", deleteConfigs.size(), event.getGuild().getIdLong());

        if (deleteConfigs.isEmpty()) {
            event.reply(getResponse("precondition.hasSavedConfigs.invalid")).queue();
            return false;
        }

        return true;
    }
}