package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.slash.HasSavedConfigs;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class GetConfigCommand implements ISubCommand {
    private final IQueryableRepository<DeleteConfig> configRepository;

    public GetConfigCommand(final IQueryableRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // command can only be executed within a guild
        // noinspection DataFlowIssue
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());

        event.reply(Formatter.response(deleteConfigs)).queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasSavedConfigs(configRepository)
        );
    }

    @Override
    public SubcommandData getSubcommandData() {
        return new SubcommandData("get", "Displays all of your create auto delete configs");
    }
}