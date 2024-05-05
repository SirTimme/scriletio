package dev.sirtimme.scriletio.commands.modal;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.modal.update.UpdateModal;
import dev.sirtimme.scriletio.preconditions.PreconditionResult;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ModalManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, ICommand<ModalInteractionEvent>>> modals;

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

		if (modal.checkPreconditions(event) == PreconditionResult.FAILURE) {
			return;
		}

		entityManager.getTransaction().begin();
		modal.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}