package dev.sirtimme.scriletio.factory.event;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.event.GuildReadyCommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

public class GuildReadyEventCommandFactory implements IEventCommandFactory<GuildReadyEvent> {
    private final DeleteTaskManager deleteTaskManager;

    public GuildReadyEventCommandFactory(final DeleteTaskManager deleteTaskManager) {
        this.deleteTaskManager = deleteTaskManager;
    }

    @Override
    public ICommand<GuildReadyEvent> createCommand(final GuildReadyEvent event, final EntityManager context) {
        return new GuildReadyCommand(deleteTaskManager, new DeleteConfigRepository(context));
    }
}