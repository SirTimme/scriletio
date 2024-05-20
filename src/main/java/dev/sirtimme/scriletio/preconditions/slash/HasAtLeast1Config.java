package dev.sirtimme.scriletio.preconditions.slash;

import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HasAtLeast1Config implements IPrecondition<SlashCommandInteractionEvent> {
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public HasAtLeast1Config(final IRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());
        if (deleteConfigs.isEmpty()) {
            event.reply("**" + event.getGuild().getName() + "** has no configs saved").queue();
            return false;
        }
        return true;
    }
}