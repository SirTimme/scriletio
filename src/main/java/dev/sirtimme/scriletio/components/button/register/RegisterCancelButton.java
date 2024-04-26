package dev.sirtimme.scriletio.components.button.register;

import dev.sirtimme.scriletio.components.button.Button;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;

public class RegisterCancelButton extends Button {
	@Override
	protected void handleCommand(final ButtonInteractionEvent event, final EntityManager entityManager) {
		event.editMessage("Registration has been cancelled").setComponents(Collections.emptyList()).queue();
	}
}