package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.iuvo.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.precondition.IPrecondition;
import dev.sirtimme.iuvo.repository.QueryableRepository;
import dev.sirtimme.iuvo.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.utils.ParsingException;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.utils.Parser;
import dev.sirtimme.scriletio.precondition.slash.IsRegistered;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;
import java.util.Locale;

public class AddConfigCommand implements ISubCommand {
    private final QueryableRepository<DeleteConfig> configRepository;
    private final Repository<User> userRepository;

    public AddConfigCommand(final QueryableRepository<DeleteConfig> configRepository, final Repository<User> userRepository) {
        this.configRepository = configRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        // noinspection DataFlowIssue command can only be executed within a guild
        final var guildId = event.getGuild().getIdLong();

        if (configRepository.findAll(guildId).size() == 25) {
            event.reply("Each guild can only have up to 25 configs").queue();
            return;
        }

        // noinspection DataFlowIssue command option 'channel' is required
        final var channelOption = event.getOption("channel").getAsChannel();
        final var channelId = channelOption.getIdLong();

        if (configRepository.get(channelId) != null) {
            event.reply("There is already a delete config for that channel!").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(channelOption, Permission.MESSAGE_MANAGE)) {
            event.reply("I'm missing the **" + Permission.MESSAGE_MANAGE.getName() + "** permission in channel " + channelOption.getAsMention() + "!").queue();
            return;
        }

        // noinspection DataFlowIssue command option 'duration' is required
        final var durationOption = event.getOption("duration").getAsString();

        var duration = 0L;
        try {
            duration = new Parser().parse(durationOption);
        } catch (ParsingException exception) {
            event.reply(Formatter.format(durationOption, exception)).queue();
            return;
        }

        if (duration == 0) {
            event.reply("Please specify a duration of at least 1 minute").queue();
            return;
        }

        final var deleteConfig = new DeleteConfig(
            event.getUser().getIdLong(),
            guildId,
            channelId,
            List.of(),
            duration
        );

        configRepository.add(deleteConfig);

        event.reply("Successfully created an auto delete config for " + channelOption.getAsMention()).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new IsRegistered(userRepository)
        );
    }

    @Override
    public SubcommandData getSubCommandData() {
        final var channelOptionData = new OptionData(
            OptionType.CHANNEL,
            "channel",
            "The channel to delete the messages in",
            true
        ).setChannelTypes(ChannelType.TEXT);

        final var durationOptionData = new OptionData(
            OptionType.STRING,
            "duration",
            "Delete messages after specified duration",
            true
        );

        return new SubcommandData("add", "Adds a new auto delete config").addOptions(channelOptionData, durationOptionData);
    }
}