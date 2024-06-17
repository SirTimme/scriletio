package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.scriletio.commands.interaction.IInteractionCommand;
import dev.sirtimme.scriletio.commands.interaction.component.button.AcceptDeletionButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.AcceptRegistrationButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.CancelDeletionButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.CancelRegistrationButton;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import dev.sirtimme.scriletio.repository.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ButtonEventCommandFactory implements IInteractionCommandFactory<ButtonInteractionEvent> {
    private final HashMap<String, Function<EntityManager, IInteractionCommand<ButtonInteractionEvent>>> buttonCommands;

    public ButtonEventCommandFactory() {
        this.buttonCommands = new HashMap<>();
        this.buttonCommands.put("registerAccept", context -> new AcceptRegistrationButton(new UserRepository(context)));
        this.buttonCommands.put("registerCancel", context -> new CancelRegistrationButton());
        this.buttonCommands.put("deleteAccept", context -> new AcceptDeletionButton(new UserRepository(context), new DeleteConfigRepository(context)));
        this.buttonCommands.put("deleteCancel", context -> new CancelDeletionButton());
    }

    @Override
    public IInteractionCommand<ButtonInteractionEvent> createCommand(final ButtonInteractionEvent event, final EntityManager context) {
        final var commandName = event.getComponentId().split(":")[1];
        return buttonCommands.get(commandName).apply(context);
    }
}