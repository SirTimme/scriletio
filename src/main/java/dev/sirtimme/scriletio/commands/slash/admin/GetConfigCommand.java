package dev.sirtimme.scriletio.commands.slash.admin;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.slash.HasAtLeast1Config;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class GetConfigCommand implements ICommand<SlashCommandInteractionEvent> {
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public GetConfigCommand(final IRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());

        event.reply(Formatter.response(deleteConfigs)).queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasAtLeast1Config(deleteConfigRepository)
        );
    }

    public static SubcommandData getSubcommandData() {
        return new SubcommandData("get", "Displays all of your create auto delete configs");
    }
}