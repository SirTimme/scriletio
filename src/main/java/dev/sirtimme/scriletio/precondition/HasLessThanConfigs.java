package dev.sirtimme.scriletio.precondition;

import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static dev.sirtimme.scriletio.localization.LocalizationManager.getResponse;

public class HasLessThanConfigs implements IPrecondition<SlashCommandInteractionEvent> {
    private final int limit;
    private final QueryableRepository<DeleteConfig> configRepository;

    public HasLessThanConfigs(final int limit, final QueryableRepository<DeleteConfig> configRepository) {
        this.limit = limit;
        this.configRepository = configRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        if (configRepository.findAll(event.getGuild().getIdLong()).size() >= limit) {
            event.reply(getResponse("precondition.hasLessThanConfigs.invalid", limit)).queue();
            return false;
        }

        return true;
    }
}
