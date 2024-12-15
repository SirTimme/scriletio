package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isComponentAuthor;

public class DeleteAcceptButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final LocalizationManager localizationManager;
    private final Repository<User> userRepository;
    private final QueryableRepository<DeleteConfig> configRepository;

    public DeleteAcceptButton(
        final Repository<User> userRepository,
        final QueryableRepository<DeleteConfig> configRepository,
        final LocalizationManager localizationManager
    ) {
        this.localizationManager = localizationManager;
        this.userRepository = userRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = userRepository.get(userId);

        userRepository.delete(user);
        configRepository.deleteAll(userId);

        event.editMessage(localizationManager.get("button.delete.accept")).setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<? super ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            isComponentAuthor()
        );
    }
}
