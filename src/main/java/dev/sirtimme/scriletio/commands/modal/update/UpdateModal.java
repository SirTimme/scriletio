package dev.sirtimme.scriletio.commands.modal.update;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.parse.Parser;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.modal.IsModalAuthor;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.Collections;
import java.util.List;

public class UpdateModal implements ICommand<ModalInteractionEvent> {
	private final IRepository<DeleteConfig> repository;

	public UpdateModal(final IRepository<DeleteConfig> repository) {
		this.repository = repository;
	}

	@Override
	public void execute(final ModalInteractionEvent event) {
		final var durationString = event.getValues().getFirst().getAsString();
		var newDuration = 0L;
		try {
			newDuration = new Parser().parse(durationString);
		} catch (ParsingException exception) {
			event.editMessage(Formatter.format(durationString, exception)).setComponents(Collections.emptyList()).queue();
			return;
		}

		if (newDuration == 0) {
			event.reply("Please specify a duration of at least 1 minute").queue();
			return;
		}

		final var channelId = event.getModalId().split(":")[2];
		final var config = repository.get(Long.parseLong(channelId));

		config.setDuration(newDuration);

		event.editMessage("Config for channel <#" + config.getChannelId() + "> was updated successfully. The new duration is **" + newDuration + "** minutes")
			 .setComponents(Collections.emptyList())
			 .queue();
	}

	@Override
	public List<IPrecondition<ModalInteractionEvent>> getPreconditions() {
		return List.of(
				new IsModalAuthor()
		);
	}
}