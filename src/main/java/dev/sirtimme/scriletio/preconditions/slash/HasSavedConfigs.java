package dev.sirtimme.scriletio.preconditions.slash;

import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HasSavedConfigs implements IPrecondition<SlashCommandInteractionEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HasSavedConfigs.class);
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public HasSavedConfigs(final IRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // command can only be executed within a guild
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());

        LOGGER.debug("Found {} configs for guild with id {}", deleteConfigs.size(), event.getGuild().getIdLong());

        if (deleteConfigs.isEmpty()) {
            event.reply("**" + event.getGuild().getName() + "** has no configs saved").queue();
            return false;
        }

        return true;
    }
}