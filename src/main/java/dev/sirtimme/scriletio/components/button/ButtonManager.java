package dev.sirtimme.scriletio.components.button;

import dev.sirtimme.scriletio.components.button.register.RegisterAcceptButton;
import dev.sirtimme.scriletio.components.button.register.RegisterCancelButton;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ButtonManager {
	private final HashMap<String, Function<EntityManager, IButton>> buttons;
	private final EntityManagerFactory entityManagerFactory;

	public ButtonManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;

		this.buttons = new HashMap<>();
		this.buttons.put("registerAccept", entityManager -> new RegisterAcceptButton(new UserRepository(entityManager)));
		this.buttons.put("registerCancel", entityManager -> new RegisterCancelButton());
	}

	public void handleCommand(final ButtonInteractionEvent event) {
		final var buttonName = event.getComponentId().split(":")[1];
		final var function = buttons.get(buttonName);
		final var entityManager = entityManagerFactory.createEntityManager();
		final var button = function.apply(entityManager);

		entityManager.getTransaction().begin();
		button.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}