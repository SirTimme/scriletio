package dev.sirtimme.scriletio.components.modal;

import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface IModal {
	void execute(final ModalInteractionEvent event, final EntityManager entityManager);
}