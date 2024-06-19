package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.interaction.slash.IsRegistered;
import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.repository.IRepository;
import dev.sirtimme.scriletio.utils.ParsingException;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.utils.Parser;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.slash.HasSavedConfigs;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class UpdateConfigCommand implements ISubCommand {
    private final IQueryableRepository<DeleteConfig> configRepository;
    private final IRepository<User> userRepository;

    public UpdateConfigCommand(final IQueryableRepository<DeleteConfig> configRepository, final IRepository<User> userRepository) {
        this.configRepository = configRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command option 'duration' is required
        final var durationOption = event.getOption("duration").getAsString();
        final long newDuration;
        try {
            newDuration = new Parser().parse(durationOption);
        } catch (ParsingException exception) {
            event.reply(Formatter.format(durationOption, exception)).queue();
            return;
        }

        // noinspection DataFlowIssue command option 'channel' is required
        final var channelOption = event.getOption("channel").getAsString();
        final long channelId;
        try {
            channelId = Long.parseLong(channelOption);
        } catch (NumberFormatException error) {
            event.reply("A config for that channel does not exist! Please use one of the suggested channels").queue();
            return;
        }

        final var deleteConfig = configRepository.get(channelId);
        deleteConfig.setDuration(newDuration);

        event.reply("Config for channel <#" + channelId + "> successfully updated. The new duration is **" + newDuration + "** minutes").queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasSavedConfigs(configRepository),
            new IsRegistered(userRepository)
        );
    }

    @Override
    public SubcommandData getSubcommandData() {
        final var channelOption = new OptionData(
            OptionType.STRING,
            "channel",
            "The channel you want to delete the config for",
            true,
            true
        );

        final var durationOption = new OptionData(
            OptionType.STRING,
            "duration",
            "The new delete duration",
            true
        );

        return new SubcommandData("update", "Updates an existing auto delete config").addOptions(channelOption, durationOption);
    }
}