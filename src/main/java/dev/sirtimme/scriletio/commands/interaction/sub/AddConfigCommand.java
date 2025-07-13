package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.HasLessThanConfigs;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.utils.Parser;
import dev.sirtimme.scriletio.exceptions.ParsingException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isRegistered;
import static dev.sirtimme.scriletio.response.Markdown.bold;

public class AddConfigCommand implements ISubCommand {
    private final QueryableRepository<DeleteConfig> configRepository;
    private final Repository<User> userRepository;
    private final LocalizationManager localizationManager;

    public AddConfigCommand(
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
        // noinspection DataFlowIssue command option 'channel' is required
        final var channelOption = event.getOption("channel").getAsChannel();
        final var channelId = channelOption.getIdLong();

        // is a config saved for this channel?
        if (configRepository.get(channelId) != null) {
            event.reply(localizationManager.get("error.channelInUse")).queue();
            return;
        }

        // noinspection DataFlowIssue command can only be executed within a guild
        if (!event.getGuild().getSelfMember().hasPermission(channelOption, Permission.MESSAGE_MANAGE)) {
            final var response = localizationManager.get(
                "error.missingPermission",
                bold(localizationManager.get("permission.manageMessages")),
                channelOption.getAsMention()
            );
            event.reply(response).queue();
            return;
        }

        // noinspection DataFlowIssue command option 'duration' is required
        final var durationOption = event.getOption("duration").getAsString();
        final long minutes;
        try {
            minutes = new Parser().parse(durationOption);
        } catch (ParsingException exception) {
            event.reply(Formatter.format(durationOption, exception)).queue();
            return;
        }

        if (minutes == 0) {
            event.reply(localizationManager.get("error.nonPositiveDuration")).queue();
            return;
        }

        final var deleteConfig = new DeleteConfig(
            event.getUser().getIdLong(),
            event.getGuild().getIdLong(),
            channelId,
            List.of(),
            minutes
        );

        configRepository.add(deleteConfig);

        event.reply(localizationManager.get("slash.auto-delete.add", channelOption.getAsMention())).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            isRegistered(userRepository, localizationManager),
            new HasLessThanConfigs(25, configRepository, localizationManager)
        );
    }

    @Override
    public SubcommandData getSubCommandData() {
        final var channelOptionData = new OptionData(
            OptionType.CHANNEL,
            localizationManager.get("auto-delete.add.channel.name", Locale.US),
            localizationManager.get("auto-delete.add.channel.description", Locale.US),
            true
        ).setChannelTypes(ChannelType.TEXT);

        final var durationOptionData = new OptionData(
            OptionType.STRING,
            localizationManager.get("auto-delete.add.duration.name", Locale.US),
            localizationManager.get("auto-delete.add.duration.description", Locale.US),
            true
        );

        return new SubcommandData(
            localizationManager.get("auto-delete.add.name", Locale.US),
            localizationManager.get("auto-delete.add.description", Locale.US)
        ).addOptions(channelOptionData, durationOptionData);
    }
}