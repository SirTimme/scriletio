package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.scriletio.commands.IInteractionCommand;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.component.button.IsButtonAuthor;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

public class AcceptRegistrationButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final IRepository<User> userRepository;

    public AcceptRegistrationButton(final IRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event) {
        userRepository.add(new User(event.getUser().getIdLong()));

        event.editMessage("You were successfully registered").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            new IsButtonAuthor()
        );
    }
}