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
import java.util.Locale;
import java.util.function.Function;

import static dev.sirtimme.scriletio.localization.LocalizationManager.getResponse;

public class SlashEventCommandFactory implements IInteractionCommandFactory<SlashCommandInteractionEvent> {
    private final HashMap<String, Function<EntityManager, ISlashCommand>> slashCommands;

    public SlashEventCommandFactory() {
        slashCommands = new HashMap<>();
        slashCommands.put(getResponse("update.name", Locale.US), context -> new UpdateCommand(this));
        slashCommands.put(getResponse("auto-delete.name", Locale.US), context -> new AutoDeleteCommand(new UserRepository(context), new DeleteConfigRepository(context)));
        slashCommands.put(getResponse("register.name", Locale.US), context -> new RegisterCommand(new UserRepository(context)));
        slashCommands.put(getResponse("delete.name", Locale.US), context -> new DeleteCommand(new UserRepository(context)));
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