package dev.sirtimme.scriletio.commands.interaction.autocomplete;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.utils.Pair;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.Locale;

public class CommandAutoCompleteCommand implements IInteractionCommand<CommandAutoCompleteInteractionEvent> {
    private final QueryableRepository<DeleteConfig> configRepository;

    public CommandAutoCompleteCommand(final QueryableRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final CommandAutoCompleteInteractionEvent event, final Locale locale) {
        // noinspection DataFlowIssue the command can only be executed within a guild
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
    public List<IPrecondition<? super CommandAutoCompleteInteractionEvent>> getPreconditions() {
        return List.of();
    }

    private Pair<String, Long> createChannelPair(final CommandAutoCompleteInteractionEvent event, final DeleteConfig config) {
        final var channelId = config.getChannelId();
        // noinspection DataFlowIssue this command can only be executed within a guild
        final var channel = event.getGuild().getChannelById(TextChannel.class, channelId);

        // noinspection DataFlowIssue only valid channel ids are saved
        return new Pair<>("# " + channel.getName(), channelId);
    }
}
