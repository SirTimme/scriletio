package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.modal.UpdateModal;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ModalCommandManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, ICommand<ModalInteractionEvent>>> modalCommands;

	public ModalCommandManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.modalCommands = new HashMap<>();
		this.modalCommands.put("update", entityManager -> new UpdateModal(new DeleteConfigRepository(entityManager)));
	}

	public void handleCommand(final ModalInteractionEvent event) {
		final var commandName = event.getModalId().split(":")[1];
		final var entityManager = entityManagerFactory.createEntityManager();
		final var modalCommand = modalCommands.get(commandName).apply(entityManager);

		if (modalCommand.hasInvalidPreconditions(event)) {
			return;
		}

		entityManager.getTransaction().begin();
		modalCommand.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}