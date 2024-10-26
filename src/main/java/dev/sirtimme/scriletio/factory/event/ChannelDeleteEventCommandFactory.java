package dev.sirtimme.scriletio.factory.event;

import dev.sirtimme.iuvo.api.commands.event.IEventCommand;
import dev.sirtimme.iuvo.api.factory.event.IEventCommandFactory;
import dev.sirtimme.scriletio.commands.event.ChannelDeleteCommand;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;

public class ChannelDeleteEventCommandFactory implements IEventCommandFactory<ChannelDeleteEvent> {
    @Override
    public IEventCommand<ChannelDeleteEvent> createCommand(final EntityManager context) {
        return new ChannelDeleteCommand(new DeleteConfigRepository(context));
    }
}
