package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.menu.DeleteMenu;
import dev.sirtimme.scriletio.commands.menu.UpdateMenu;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class MenuCommandFactory implements ICommandFactory<StringSelectInteractionEvent> {
    private final HashMap<String, Function<EntityManager, ICommand<StringSelectInteractionEvent>>> menuCommands;

    public MenuCommandFactory() {
        this.menuCommands = new HashMap<>();
        this.menuCommands.put("update", context -> new UpdateMenu());
        this.menuCommands.put("delete", context -> new DeleteMenu(new UserRepository(context)));
    }

    @Override
    public ICommand<StringSelectInteractionEvent> createCommand(final StringSelectInteractionEvent event, final EntityManager context) {
        final var commandName = event.getComponentId().split(":")[1];
        return menuCommands.get(commandName).apply(context);
    }
}