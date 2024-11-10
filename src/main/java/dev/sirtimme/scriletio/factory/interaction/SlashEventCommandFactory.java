package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.factory.interaction.IInteractionCommandFactory;
import dev.sirtimme.scriletio.commands.interaction.slash.*;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import dev.sirtimme.scriletio.repository.UserRepository;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class SlashEventCommandFactory implements IInteractionCommandFactory<SlashCommandInteractionEvent> {
    private final HashMap<String, Function<EntityManager, ISlashCommand>> slashCommands;
    private final LocalizationManager l10nManager;

    public SlashEventCommandFactory(final LocalizationManager l10nManager) {
        this.slashCommands = new HashMap<>();
        this.l10nManager = l10nManager;

        registerSlashCommand("update.name", _ -> new UpdateCommand(this, l10nManager));
        registerSlashCommand("auto-delete.name", context -> new AutoDeleteCommand(new UserRepository(context), new DeleteConfigRepository(context), l10nManager));
        registerSlashCommand("register.name", context -> new RegisterCommand(new UserRepository(context), l10nManager));
        registerSlashCommand("delete.name", context -> new DeleteCommand(new UserRepository(context), l10nManager));
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

    private void registerSlashCommand(final String key, final Function<EntityManager, ISlashCommand> value) {
        slashCommands.put(l10nManager.get(key, Locale.US), value);
    }
}