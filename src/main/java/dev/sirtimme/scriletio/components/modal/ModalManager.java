package dev.sirtimme.scriletio.components.modal;

import dev.sirtimme.scriletio.components.modal.update.UpdateModal;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;

public class ModalManager {
	private final HashMap<String, IModal> modals;

	public ModalManager(final IRepository<DeleteConfig> repository) {
		this.modals = new HashMap<>();
		this.modals.put("update", new UpdateModal(repository));
	}

	public void handleCommand(final ModalInteractionEvent event) {
		final var modalName = event.getModalId().split(":")[1];
		final var modal = modals.get(modalName);

		modal.execute(event);
	}
}