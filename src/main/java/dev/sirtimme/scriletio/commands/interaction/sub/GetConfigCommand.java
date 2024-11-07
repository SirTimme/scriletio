package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.precondition.HasSavedConfigs;
import dev.sirtimme.scriletio.utils.Formatter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class GetConfigCommand implements ISubCommand {
    private final QueryableRepository<DeleteConfig> configRepository;

    public GetConfigCommand(final QueryableRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());

        event.reply(Formatter.response(deleteConfigs)).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasSavedConfigs(configRepository)
        );
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData("get", "Displays all of your create auto delete configs");
    }
}