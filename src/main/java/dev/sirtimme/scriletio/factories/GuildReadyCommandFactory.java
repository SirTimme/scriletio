package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.event.GuildReadyCommand;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

public class GuildReadyCommandFactory implements ICommandFactory<GuildReadyEvent> {
    private final DeleteTaskManager deleteTaskManager;

    public GuildReadyCommandFactory(final DeleteTaskManager deleteTaskManager) {
        this.deleteTaskManager = deleteTaskManager;
    }

    @Override
    public ICommand<GuildReadyEvent> createCommand(final GuildReadyEvent event, final EntityManager context) {
        return new GuildReadyCommand(deleteTaskManager, new DeleteConfigRepository(context));
    }
}