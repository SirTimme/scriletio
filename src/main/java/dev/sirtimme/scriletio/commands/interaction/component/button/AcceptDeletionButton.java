package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AcceptDeletionButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final Repository<User> userRepository;
    private final QueryableRepository<DeleteConfig> configRepository;

    public AcceptDeletionButton(final Repository<User> userRepository, final QueryableRepository<DeleteConfig> configRepository) {
        this.userRepository = userRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        final var userId = event.getUser().getIdLong();
        final var user = userRepository.get(userId);

        userRepository.delete(user);
        configRepository.deleteAll(userId);

        event.editMessage("All of your stored data is gone").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<? super ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            IPrecondition.isComponentAuthor()
        );
    }
}
