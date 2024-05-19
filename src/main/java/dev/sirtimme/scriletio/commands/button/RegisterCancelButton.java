package dev.sirtimme.scriletio.commands.button;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.button.IsButtonAuthor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

public class RegisterCancelButton implements ICommand<ButtonInteractionEvent> {
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