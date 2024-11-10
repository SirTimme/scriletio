package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.factory.interaction.IInteractionCommandFactory;
import dev.sirtimme.scriletio.commands.interaction.component.button.AcceptDeletionButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.AcceptRegistrationButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.CancelDeletionButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.CancelRegistrationButton;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import dev.sirtimme.scriletio.repository.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ButtonEventCommandFactory implements IInteractionCommandFactory<ButtonInteractionEvent> {
    private final HashMap<String, Function<EntityManager, IInteractionCommand<ButtonInteractionEvent>>> buttonCommands;

    public ButtonEventCommandFactory(final LocalizationManager l10nManager) {
        this.buttonCommands = new HashMap<>();

        registerButtonCommand("register-accept", context -> new AcceptRegistrationButton(new UserRepository(context), l10nManager));
        registerButtonCommand("register-cancel", _ -> new CancelRegistrationButton(l10nManager));
        registerButtonCommand("delete-accept", context -> new AcceptDeletionButton(new UserRepository(context), new DeleteConfigRepository(context), l10nManager));
        registerButtonCommand("delete-cancel", _ -> new CancelDeletionButton(l10nManager));
    }

    @Override
    public IInteractionCommand<ButtonInteractionEvent> createCommand(final ButtonInteractionEvent event, final EntityManager context) {
        final var commandName = event.getComponentId().split(":")[1];
        return buttonCommands.get(commandName).apply(context);
    }

    private void registerButtonCommand(final String key, final Function<EntityManager, IInteractionCommand<ButtonInteractionEvent>> value) {
        buttonCommands.put(key, value);
    }
}