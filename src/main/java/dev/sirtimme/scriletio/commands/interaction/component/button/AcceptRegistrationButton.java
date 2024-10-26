package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AcceptRegistrationButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final Repository<User> userRepository;

    public AcceptRegistrationButton(final Repository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        userRepository.add(new User(event.getUser().getIdLong()));

        event.editMessage("You were successfully registered").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<? super ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            IPrecondition.isComponentAuthor()
        );
    }
}