package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.utils.Pair;
import dev.sirtimme.scriletio.commands.IInteractionCommand;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;

public class CommandAutoCompleteCommand implements IInteractionCommand<CommandAutoCompleteInteractionEvent> {
    private final IQueryableRepository<DeleteConfig> configRepository;

    public CommandAutoCompleteCommand(final IQueryableRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final CommandAutoCompleteInteractionEvent event) {
        // the corresponding command can only be executed within a guild
        final var guildId = event.getGuild().getIdLong();
        final var deleteConfigs = configRepository.findAll(guildId);

        final var choices = deleteConfigs
            .stream()
            .map(config -> new Pair<>("# " + event.getGuild().getChannelById(TextChannel.class, config.getChannelId()).getName(), config.getChannelId()))
            .filter(pair -> pair.first().contains(event.getFocusedOption().getValue()))
            .map(pair -> new Command.Choice(pair.first(), pair.second()))
            .toList();

        event.replyChoices(choices).queue();
    }

    @Override
    public List<IPrecondition<CommandAutoCompleteInteractionEvent>> getPreconditions() {
        return List.of();
    }
}
