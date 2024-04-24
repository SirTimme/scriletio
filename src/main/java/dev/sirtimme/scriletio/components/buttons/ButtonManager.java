package dev.sirtimme.scriletio.components.buttons;

import dev.sirtimme.scriletio.components.buttons.register.RegisterAccept;
import dev.sirtimme.scriletio.components.buttons.register.RegisterCancel;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;

public class ButtonManager {
	private final HashMap<String, IButton> buttons;

	public ButtonManager(final IRepository<User> repository) {
		this.buttons = new HashMap<>();
		this.buttons.put("registerAccept", new RegisterAccept(repository));
		this.buttons.put("registerCancel", new RegisterCancel());
	}

	public void handleCommand(final ButtonInteractionEvent event) {
		final var buttonName = event.getComponentId().split(":")[1];
		final var button = buttons.get(buttonName);

		button.execute(event);
	}
}