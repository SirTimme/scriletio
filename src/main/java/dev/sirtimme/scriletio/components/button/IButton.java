package dev.sirtimme.scriletio.components.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface IButton {
	void execute(final ButtonInteractionEvent event);
}