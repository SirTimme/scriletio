package dev.sirtimme.scriletio.components.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface IButton {
	void execute(final ButtonInteractionEvent event);
}