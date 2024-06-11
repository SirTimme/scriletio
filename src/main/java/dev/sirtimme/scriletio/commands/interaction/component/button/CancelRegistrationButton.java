package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.scriletio.commands.IInteractionCommand;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.component.button.IsButtonAuthor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

public class CancelRegistrationButton implements IInteractionCommand<ButtonInteractionEvent> {
    @Override
    public void execute(final ButtonInteractionEvent event) {
        event.editMessage("Registration has been cancelled").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            new IsButtonAuthor()
        );
    }
}