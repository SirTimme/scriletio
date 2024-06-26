package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;

public class ChannelDeleteCommand implements ICommand<ChannelDeleteEvent> {
    private final IRepository<DeleteConfig> configRepository;

    public ChannelDeleteCommand(final IRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final ChannelDeleteEvent event) {
        final var deleteConfig = configRepository.get(event.getChannel().getIdLong());

        if (deleteConfig == null) {
            return;
        }

        configRepository.delete(deleteConfig);
    }
}
