package dev.sirtimme.scriletio.commands.interaction.component.menu;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;
import java.util.List;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isComponentAuthor;

public class DeleteMenu implements IInteractionCommand<StringSelectInteractionEvent> {
    private final LocalizationManager l10nManager;
    private final Repository<DeleteConfig> configRepository;

    public DeleteMenu(final LocalizationManager l10nManager, final Repository<DeleteConfig> configRepository) {
        this.l10nManager = l10nManager;
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
    public List<IPrecondition<? super StringSelectInteractionEvent>> getPreconditions() {
        return List.of(
            isComponentAuthor()
        );
    }
}