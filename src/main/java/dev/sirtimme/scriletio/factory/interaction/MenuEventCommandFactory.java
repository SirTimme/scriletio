package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.factory.interaction.IInteractionCommandFactory;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.scriletio.commands.interaction.component.menu.DeleteMenu;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class MenuEventCommandFactory implements IInteractionCommandFactory<StringSelectInteractionEvent> {
    private final HashMap<String, Function<EntityManager, IInteractionCommand<StringSelectInteractionEvent>>> menuCommands;

    public MenuEventCommandFactory(final LocalizationManager l10nManager) {
        this.menuCommands = new HashMap<>();
        this.menuCommands.put("delete", context -> new DeleteMenu(l10nManager, new DeleteConfigRepository(context)));
    }

    @Override
    public IInteractionCommand<StringSelectInteractionEvent> createCommand(final StringSelectInteractionEvent event, final EntityManager context) {
        final var commandName = event.getComponentId().split(":")[1];
        return menuCommands.get(commandName).apply(context);
    }
}