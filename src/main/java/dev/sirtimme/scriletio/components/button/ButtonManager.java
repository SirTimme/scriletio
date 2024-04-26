package dev.sirtimme.scriletio.components.button;

import dev.sirtimme.scriletio.components.button.register.RegisterAcceptButton;
import dev.sirtimme.scriletio.components.button.register.RegisterCancelButton;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;

public class ButtonManager {
	private final HashMap<String, IButton> buttons;
	private final EntityManagerFactory entityManagerFactory;

	public ButtonManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;

		this.buttons = new HashMap<>();
		this.buttons.put("registerAccept", new RegisterAcceptButton());
		this.buttons.put("registerCancel", new RegisterCancelButton());
	}

	public void handleCommand(final ButtonInteractionEvent event) {
		final var buttonName = event.getComponentId().split(":")[1];
		final var button = buttons.get(buttonName);
		final var entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		button.execute(event, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}