package dev.sirtimme.scriletio.components.buttons.register;

import dev.sirtimme.scriletio.components.buttons.MessageButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;

public class RegisterCancel extends MessageButton {
    @Override
    protected void handleCommand(final ButtonInteractionEvent event) {
        event.editMessage("Registration has been cancelled").setComponents(Collections.emptyList()).queue();
    }
}