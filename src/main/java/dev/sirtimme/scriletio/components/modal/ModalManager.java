package dev.sirtimme.scriletio.components.modal;

import dev.sirtimme.scriletio.components.modal.update.UpdateModal;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;

public class ModalManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, IModal> modals;

	public ModalManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.modals = new HashMap<>();
		this.modals.put("update", new UpdateModal());
	}

	public void handleCommand(final ModalInteractionEvent event) {
		final var modalName = event.getModalId().split(":")[1];
		final var modal = modals.get(modalName);
		final var entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		modal.execute(event, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}