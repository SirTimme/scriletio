package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.scriletio.commands.interaction.IInteractionCommand;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.component.button.IsButtonAuthor;
import dev.sirtimme.scriletio.repository.IQueryableRepository;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

public class AcceptDeletionButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final IRepository<User> userRepository;
    private final IQueryableRepository<DeleteConfig> configRepository;

    public AcceptDeletionButton(final IRepository<User> userRepository, final IQueryableRepository<DeleteConfig> configRepository) {
        this.userRepository = userRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = userRepository.get(userId);

        userRepository.delete(user);
        configRepository.deleteAll(userId);

        event.editMessage("All of your stored data is gone").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            new IsButtonAuthor()
        );
    }
}
