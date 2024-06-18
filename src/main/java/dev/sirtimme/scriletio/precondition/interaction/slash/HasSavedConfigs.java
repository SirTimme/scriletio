package dev.sirtimme.scriletio.precondition.interaction.slash;

import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.repository.IQueryableRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HasSavedConfigs implements IPrecondition<SlashCommandInteractionEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HasSavedConfigs.class);
    private final IQueryableRepository<DeleteConfig> deleteConfigRepository;

    public HasSavedConfigs(final IQueryableRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // command can only be executed within a guild
        // noinspection DataFlowIssue
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());

        LOGGER.debug("Found {} configs for guild with id {}", deleteConfigs.size(), event.getGuild().getIdLong());

        if (deleteConfigs.isEmpty()) {
            event.reply("**" + event.getGuild().getName() + "** has no configs saved").queue();
            return false;
        }

        return true;
    }
}