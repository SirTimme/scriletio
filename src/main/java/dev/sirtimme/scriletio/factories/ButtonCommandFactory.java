package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.button.RegisterAcceptButton;
import dev.sirtimme.scriletio.commands.button.RegisterCancelButton;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ButtonCommandFactory implements ICommandFactory<ButtonInteractionEvent> {
    private final HashMap<String, Function<EntityManager, ICommand<ButtonInteractionEvent>>> buttonCommands;

    public ButtonCommandFactory() {
        this.buttonCommands = new HashMap<>();
        this.buttonCommands.put("registerAccept", context -> new RegisterAcceptButton(new UserRepository(context)));
        this.buttonCommands.put("registerCancel", context -> new RegisterCancelButton());
    }

    @Override
    public ICommand<ButtonInteractionEvent> createCommand(final ButtonInteractionEvent event, final EntityManager context) {
        final var commandName = event.getComponentId().split(":")[1];
        return buttonCommands.get(commandName).apply(context);
    }
}