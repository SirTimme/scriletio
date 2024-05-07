package dev.sirtimme.scriletio.commands.menu;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.menu.IsMenuAuthor;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class UpdateMenu implements ICommand<StringSelectInteractionEvent> {
	@Override
	public void execute(final StringSelectInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var configId = event.getValues().getFirst();
		final var durationInput = TextInput.create("duration", "Duration", TextInputStyle.SHORT).build();
		final var modal = Modal.create(userId + ":update:" + configId, "Specify a new delete duration").addComponents(ActionRow.of(durationInput)).build();

		event.replyModal(modal).queue();
	}

	@Override
	public List<IPrecondition<StringSelectInteractionEvent>> getPreconditions() {
		return List.of(
				new IsMenuAuthor()
		);
	}
}