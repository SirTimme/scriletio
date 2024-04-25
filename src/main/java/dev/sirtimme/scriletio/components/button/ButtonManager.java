package dev.sirtimme.scriletio.components.button;

import dev.sirtimme.scriletio.components.button.register.RegisterAcceptButton;
import dev.sirtimme.scriletio.components.button.register.RegisterCancelButton;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;

public class ButtonManager {
	private final HashMap<String, IButton> buttons;

	public ButtonManager(final IRepository<User> repository) {
		this.buttons = new HashMap<>();
		this.buttons.put("registerAccept", new RegisterAcceptButton(repository));
		this.buttons.put("registerCancel", new RegisterCancelButton());
	}

	public void handleCommand(final ButtonInteractionEvent event) {
		final var buttonName = event.getComponentId().split(":")[1];
		final var button = buttons.get(buttonName);

		button.execute(event);
	}
}