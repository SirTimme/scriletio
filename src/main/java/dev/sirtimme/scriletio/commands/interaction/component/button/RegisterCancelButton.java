package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isComponentAuthor;

public class RegisterCancelButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final LocalizationManager localizationManager;

    public RegisterCancelButton(final LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
    }

    @Override
    public void execute(final ButtonInteractionEvent event) {
        final var response = localizationManager.get("button.register.cancel");
        event.editMessage(response).setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<? super ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            isComponentAuthor()
        );
    }
}