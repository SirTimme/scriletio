package dev.sirtimme.scriletio.factory.interaction;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.factory.interaction.IInteractionCommandFactory;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.scriletio.commands.interaction.component.button.DeleteAcceptButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.RegisterAcceptButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.DeleteCancelButton;
import dev.sirtimme.scriletio.commands.interaction.component.button.RegisterCancelButton;
import dev.sirtimme.scriletio.repository.DeleteConfigRepository;
import dev.sirtimme.scriletio.repository.UserRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ButtonEventCommandFactory implements IInteractionCommandFactory<ButtonInteractionEvent> {
    private final HashMap<String, Function<EntityManager, IInteractionCommand<ButtonInteractionEvent>>> buttonCommands;

    public ButtonEventCommandFactory(final LocalizationManager localizationManager) {
        this.buttonCommands = new HashMap<>();

        registerButtonCommand("register-accept", context -> new RegisterAcceptButton(new UserRepository(context), localizationManager));
        registerButtonCommand("register-cancel", _ -> new RegisterCancelButton(localizationManager));
        registerButtonCommand("delete-accept", context -> new DeleteAcceptButton(new UserRepository(context), new DeleteConfigRepository(context), localizationManager));
        registerButtonCommand("delete-cancel", _ -> new DeleteCancelButton(localizationManager));
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