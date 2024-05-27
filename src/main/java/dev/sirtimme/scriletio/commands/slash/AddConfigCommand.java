package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.parse.Parser;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class AddConfigCommand implements ICommand<SlashCommandInteractionEvent> {
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public AddConfigCommand(final IRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // command can only be executed within a guild
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());

        if (deleteConfigs.size() == 25) {
            event.reply("Each guild can only have up to 25 configs, please delete an old one to create a new one").queue();
            return;
        }

        // command option is required
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

        // command option is required
        final var channelOption = event.getOption("channel").getAsChannel();
        final var config = deleteConfigRepository.get(channelOption.getIdLong());

        if (config != null) {
            event.reply("There is already a delete config for that channel!").queue();
            return;
        }

        final var deleteConfig = new DeleteConfig(
            event.getUser().getIdLong(),
            event.getGuild().getIdLong(),
            channelOption.getIdLong(),
            List.of(),
            duration
        );

        deleteConfigRepository.add(deleteConfig);

        event.reply("Successfully created an auto delete config for **" + channelOption.getAsMention() + "**").queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of();
    }

    public static SubcommandData getSubcommandData() {
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