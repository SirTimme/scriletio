package dev.sirtimme.scriletio.commands.event;

import dev.sirtimme.iuvo.api.commands.event.IEventCommand;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;

public class ChannelDeleteCommand implements IEventCommand<ChannelDeleteEvent> {
    private final Repository<DeleteConfig> configRepository;

    public ChannelDeleteCommand(final Repository<DeleteConfig> configRepository) {
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
