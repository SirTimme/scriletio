package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.event.ChannelDeleteCommand;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;

public class ChannelDeleteCommandFactory implements ICommandFactory<ChannelDeleteEvent> {
    @Override
    public ICommand<ChannelDeleteEvent> createCommand(final ChannelDeleteEvent event, final EntityManager context) {
        return new ChannelDeleteCommand(new DeleteConfigRepository(context));
    }
}
