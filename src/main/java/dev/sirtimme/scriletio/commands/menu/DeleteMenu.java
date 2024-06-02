package dev.sirtimme.scriletio.commands.menu;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.menu.IsMenuAuthor;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;
import java.util.List;

public class DeleteMenu implements ICommand<StringSelectInteractionEvent> {
    private final IRepository<DeleteConfig> configRepository;

    public DeleteMenu(final IRepository<DeleteConfig> configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final StringSelectInteractionEvent event) {
        final var channelId = event.getValues().getFirst();
        final var deleteConfig = configRepository.get(Long.parseLong(channelId));

        configRepository.delete(deleteConfig);

        event.editMessage("Config for channel <#" + channelId + "> was successfully deleted").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<StringSelectInteractionEvent>> getPreconditions() {
        return List.of(
            new IsMenuAuthor()
        );
    }
}