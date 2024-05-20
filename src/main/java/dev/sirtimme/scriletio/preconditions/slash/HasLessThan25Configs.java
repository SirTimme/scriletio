package dev.sirtimme.scriletio.preconditions.slash;

import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HasLessThan25Configs implements IPrecondition<SlashCommandInteractionEvent> {
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public HasLessThan25Configs(final IRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());
        if (deleteConfigs.size() == 25) {
            event.reply("Each guild can only have up to 25 configs, please delete an old one to create a new one").queue();
            return false;
        }
        return true;
    }
}