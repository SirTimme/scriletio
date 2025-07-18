package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.HasSavedConfigs;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.utils.Parser;
import dev.sirtimme.scriletio.exceptions.ParsingException;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isRegistered;
import static dev.sirtimme.scriletio.response.Markdown.bold;
import static dev.sirtimme.scriletio.response.Markdown.channel;

public class UpdateConfigCommand implements ISubCommand {
    private final QueryableRepository<DeleteConfig> configRepository;
    private final Repository<User> userRepository;
    private final LocalizationManager localizationManager;

    public UpdateConfigCommand(
        final QueryableRepository<DeleteConfig> configRepository,
        final Repository<User> userRepository,
        final LocalizationManager localizationManager
    ) {
        this.configRepository = configRepository;
        this.userRepository = userRepository;
        this.localizationManager = localizationManager;
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
        final var channelOption = event.getOption("channel").getAsChannel();
        final var channelId = channelOption.getIdLong();

        final var deleteConfig = configRepository.get(channelId);
        if (deleteConfig == null) {
            event.reply(localizationManager.get("error.invalidChannel")).queue();
            return;
        }

        deleteConfig.setDuration(newDuration);

        final var response = localizationManager.get(
            "slash.auto-delete.update",
            channel(channelId),
            bold(newDuration)
        );

        event.reply(response).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasSavedConfigs(configRepository, localizationManager),
            isRegistered(userRepository, localizationManager)
        );
    }

    @Override
    public SubcommandData getSubCommandData() {
        final var channelOption = new OptionData(
            OptionType.CHANNEL,
            localizationManager.get("auto-delete.update.channel.name", Locale.US),
            localizationManager.get("auto-delete.update.channel.description", Locale.US),
            true
        ).setChannelTypes(ChannelType.TEXT);

        final var durationOption = new OptionData(
            OptionType.STRING,
            localizationManager.get("auto-delete.update.duration.name", Locale.US),
            localizationManager.get("auto-delete.update.duration.description", Locale.US),
            true
        );

        return new SubcommandData(
            localizationManager.get("auto-delete.update.name", Locale.US),
            localizationManager.get("auto-delete.update.description", Locale.US)
        ).addOptions(channelOption, durationOption);
    }
}