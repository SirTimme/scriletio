package dev.sirtimme.scriletio.components.menu.update;

import dev.sirtimme.scriletio.components.menu.Menu;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class UpdateMenu extends Menu {
	@Override
	protected void handleCommand(final StringSelectInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var configId = event.getValues().getFirst();
		final var durationInput = TextInput.create("duration", "Duration", TextInputStyle.SHORT).build();
		final var modal = Modal.create(userId + ":update:" + configId, "Specify a new delete duration").addComponents(ActionRow.of(durationInput)).build();

		event.replyModal(modal).queue();
	}
}