package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CancelRegistrationButton implements IInteractionCommand<ButtonInteractionEvent> {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        event.editMessage("Registration has been cancelled").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<? super ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            IPrecondition.isComponentAuthor()
        );
    }
}