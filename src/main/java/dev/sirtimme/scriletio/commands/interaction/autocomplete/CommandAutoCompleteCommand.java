package dev.sirtimme.scriletio.commands.interaction.autocomplete;

import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.utils.Pair;
import dev.sirtimme.scriletio.commands.interaction.IInteractionCommand;
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
        // the command can only be executed within a guild
        // noinspection DataFlowIssue
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());

        final var choices = deleteConfigs
            .stream()
            .map(config -> createChannelPair(event, config))
            .filter(pair -> pair.first().contains(event.getFocusedOption().getValue()))
            .map(pair -> new Command.Choice(pair.first(), pair.second()))
            .toList();

        event.replyChoices(choices).queue();
    }

    @Override
    public List<IPrecondition<CommandAutoCompleteInteractionEvent>> getPreconditions() {
        return List.of();
    }

    private Pair<String, Long> createChannelPair(final CommandAutoCompleteInteractionEvent event, final DeleteConfig config) {
        final var channelId = config.getChannelId();
        // only valid channel ids are saved
        // noinspection DataFlowIssue
        return new Pair<>("# " + event.getGuild().getChannelById(TextChannel.class, channelId).getName(), channelId);
    }
}
