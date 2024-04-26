package dev.sirtimme.scriletio.components.menu;

import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface IMenu {
	void execute(final StringSelectInteractionEvent event, final EntityManager entityManager);
}