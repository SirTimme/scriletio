package dev.sirtimme.scriletio.components.modal.update;

import dev.sirtimme.scriletio.components.modal.Modal;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.parse.Parser;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.Collections;

public class UpdateModal extends Modal {
	@Override
	protected void handleCommand(final ModalInteractionEvent event, final EntityManager entityManager) {
		final var repository = new DeleteConfigRepository(entityManager);

		final var durationString = event.getValues().getFirst().getAsString();
		var newDuration = 0L;
		try {
			newDuration = new Parser().parse(durationString);
		} catch (ParsingException exception) {
			event.editMessage(Formatter.format(durationString, exception)).setComponents(Collections.emptyList()).queue();
			return;
		}

		final var channelId = event.getModalId().split(":")[2];
		final var config = repository.get(Long.parseLong(channelId));

		config.setDuration(newDuration);
		repository.update(config);

		event.editMessage("Config for channel <#" + config.getChannelId() + "> was updated successfully. The new duration is **" + config.getDuration() + "** minutes")
			 .setComponents(Collections.emptyList())
			 .queue();
	}
}