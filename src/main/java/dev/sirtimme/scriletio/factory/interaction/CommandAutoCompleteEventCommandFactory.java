package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.scriletio.commands.interaction.IInteractionCommand;
import dev.sirtimme.scriletio.commands.event.CommandAutoCompleteCommand;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

public class CommandAutoCompleteEventCommandFactory implements IInteractionCommandFactory<CommandAutoCompleteInteractionEvent> {
    @Override
    public IInteractionCommand<CommandAutoCompleteInteractionEvent> createCommand(final CommandAutoCompleteInteractionEvent event, final EntityManager context) {
        return new CommandAutoCompleteCommand(new DeleteConfigRepository(context));
    }
}
