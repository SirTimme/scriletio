package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.factory.interaction.IInteractionCommandFactory;
import dev.sirtimme.scriletio.commands.interaction.slash.*;
import dev.sirtimme.scriletio.repository.UserRepository;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class SlashEventCommandFactory implements IInteractionCommandFactory<SlashCommandInteractionEvent> {
    private final HashMap<String, Function<EntityManager, ISlashCommand>> slashCommands;

    public SlashEventCommandFactory() {
        this.slashCommands = new HashMap<>();
        this.slashCommands.put("update", context -> new UpdateCommand(this));
        this.slashCommands.put("autodelete", context -> new AutoDeleteCommand(new UserRepository(context), new DeleteConfigRepository(context)));
        this.slashCommands.put("register", context -> new RegisterCommand(new UserRepository(context)));
        this.slashCommands.put("delete", context -> new DeleteCommand(new UserRepository(context)));
    }

    @Override
    public IInteractionCommand<SlashCommandInteractionEvent> createCommand(final SlashCommandInteractionEvent event, final EntityManager context) {
        return slashCommands.get(event.getName()).apply(context);
    }

    public List<CommandData> getCommandData() {
        return slashCommands
            .values()
            .stream()
            .map(function -> function.apply(null).getCommandData())
            .toList();
    }
}