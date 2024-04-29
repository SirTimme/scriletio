package dev.sirtimme.scriletio.components.modal;

import dev.sirtimme.scriletio.components.modal.update.UpdateModal;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ModalManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, IModal>> modals;

	public ModalManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.modals = new HashMap<>();
		this.modals.put("update", entityManager -> new UpdateModal(new DeleteConfigRepository(entityManager)));
	}

	public void handleCommand(final ModalInteractionEvent event) {
		final var modalName = event.getModalId().split(":")[1];
		final var function = modals.get(modalName);
		final var entityManager = entityManagerFactory.createEntityManager();
		final var modal = function.apply(entityManager);

		entityManager.getTransaction().begin();
		modal.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}