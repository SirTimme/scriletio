package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuildReadyCommand implements ICommand<GuildReadyEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildReadyCommand.class);
    private final DeleteTaskManager deleteTaskManager;
    private final IRepository<DeleteConfig> configRepository;

    public GuildReadyCommand(final DeleteTaskManager deleteTaskManager, final IRepository<DeleteConfig> configRepository) {
        this.deleteTaskManager = deleteTaskManager;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final GuildReadyEvent event) {
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());
        LOGGER.debug("Found {} configs for guild with id {}", deleteConfigs.size(), event.getGuild().getIdLong());

        for (final var deleteConfig : deleteConfigs) {
            final var channel = event.getGuild().getChannelById(TextChannel.class, deleteConfig.getChannelId());

            if (channel == null) {
                LOGGER.warn("Could not retrieve channel with id {}", deleteConfig.getChannelId());
                continue;
            }

            for (final var deleteTask : deleteConfig.getDeleteTasks()) {
                channel.retrieveMessageById(deleteTask.getMessageId()).queue(
                    message -> {
                        LOGGER.debug("Submitted delete task for message with id {}", deleteTask.getMessageId());
                        deleteTaskManager.submitTask(deleteTask, message);
                    },
                    error -> {
                        LOGGER.warn("Could not retrieve message with id {}", deleteTask.getMessageId());
                        // TODO test this..
                        deleteConfig.getDeleteTasks().remove(deleteTask);
                    }
                );
            }
        }
    }

    @Override
    public List<IPrecondition<GuildReadyEvent>> getPreconditions() {
        return List.of();
    }
}