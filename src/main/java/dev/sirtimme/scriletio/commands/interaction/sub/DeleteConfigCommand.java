package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.slash.HasSavedConfigs;
import dev.sirtimme.scriletio.precondition.interaction.slash.IsRegistered;
import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static dev.sirtimme.scriletio.utils.TimeUtils.createReadableDuration;

public class DeleteConfigCommand implements ISubCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteConfigCommand.class);
    private final IQueryableRepository<DeleteConfig> configRepository;
    private final IRepository<User> userRepository;

    public DeleteConfigCommand(final IQueryableRepository<DeleteConfig> configRepository, final IRepository<User> userRepository) {
        this.configRepository = configRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());
        final var deleteMenuBuilder = StringSelectMenu.create(event.getUser().getIdLong() + ":" + "delete").setPlaceholder("Saved configs");

        for (final var config : deleteConfigs) {
            final var channel = event.getGuild().getChannelById(TextChannel.class, config.getChannelId());

            if (channel == null) {
                LOGGER.warn("Could not retrieve channel with id {}: Result was null", config.getChannelId());
                continue;
            }

            final var channelName = "#" + channel.getName();
            final var value = String.valueOf(config.getChannelId());
            final var description = createReadableDuration(config.getDuration());

            deleteMenuBuilder.addOption(channelName, value, description, Emoji.fromUnicode("U+1F4D1"));
        }

        event.reply("Please select the config you want to delete").addActionRow(deleteMenuBuilder.build()).queue();
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
        return new SubcommandData("delete", "Deletes an existing auto delete config");
    }
}