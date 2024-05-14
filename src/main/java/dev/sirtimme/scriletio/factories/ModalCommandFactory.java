package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.modal.UpdateModal;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;
import java.util.function.Function;

public class ModalCommandFactory implements ICommandFactory<ModalInteractionEvent> {
	private final HashMap<String, Function<EntityManager, ICommand<ModalInteractionEvent>>> modalCommands;

	public ModalCommandFactory() {
		this.modalCommands = new HashMap<>();
		this.modalCommands.put("update", entityManager -> new UpdateModal(new DeleteConfigRepository(entityManager)));
	}

	@Override
	public ICommand<ModalInteractionEvent> createCommand(final ModalInteractionEvent event, final EntityManager context) {
		final var commandName = event.getModalId().split(":")[1];
		return modalCommands.get(commandName).apply(context);
	}
}