package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.HasSavedConfigs;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.utils.Parser;
import dev.sirtimme.scriletio.exceptions.ParsingException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isRegistered;

public class UpdateConfigCommand implements ISubCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateConfigCommand.class);
    private final QueryableRepository<DeleteConfig> configRepository;
    private final Repository<User> userRepository;

    public UpdateConfigCommand(final QueryableRepository<DeleteConfig> configRepository, final Repository<User> userRepository) {
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

        LOGGER.info("Updated config for channel <#{}>: The new duration is {} minutes", channelId, newDuration);
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasSavedConfigs(configRepository),
            isRegistered(userRepository)
        );
    }

    @Override
    public SubcommandData getSubCommandData() {
        final var channelOption = new OptionData(
            OptionType.STRING,
            "channel",
            "The channel you want to update the config for",
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