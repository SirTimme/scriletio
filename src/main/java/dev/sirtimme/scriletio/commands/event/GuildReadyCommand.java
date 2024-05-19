package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.models.DeleteTask;
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
    private final IRepository<DeleteTask> taskRepository;
    private final IRepository<DeleteConfig> configRepository;

    public GuildReadyCommand(
        final DeleteTaskManager deleteTaskManager,
        final IRepository<DeleteTask> taskRepository,
        final IRepository<DeleteConfig> configRepository
    ) {
        this.deleteTaskManager = deleteTaskManager;
        this.taskRepository = taskRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final GuildReadyEvent event) {
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());

        for (final var deleteConfig : deleteConfigs) {
            final var channel = event.getGuild().getChannelById(TextChannel.class, deleteConfig.getChannelId());

            for (final var deleteTask : taskRepository.findAll(channel.getIdLong())) {
                channel.retrieveMessageById(deleteTask.getMessageId()).queue(
                    message -> deleteTaskManager.submitTask(deleteTask, message),
                    error -> LOGGER.warn("Could not find a message with id {}, removing from db", deleteTask.getMessageId())
                );

                taskRepository.delete(deleteTask);
            }
        }
    }

    @Override
    public List<IPrecondition<GuildReadyEvent>> getPreconditions() {
        return List.of();
    }
}