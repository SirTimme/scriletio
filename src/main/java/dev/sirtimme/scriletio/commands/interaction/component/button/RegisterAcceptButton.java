package dev.sirtimme.scriletio.commands.interaction.component.button;

import dev.sirtimme.iuvo.api.commands.interaction.IInteractionCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isComponentAuthor;

public class RegisterAcceptButton implements IInteractionCommand<ButtonInteractionEvent> {
    private final LocalizationManager localizationManager;
    private final Repository<User> userRepository;

    public RegisterAcceptButton(final Repository<User> userRepository, final LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event) {
        userRepository.add(new User(event.getUser().getIdLong()));

        event.editMessage(localizationManager.get("button.register.accept")).setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<? super ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            isComponentAuthor()
        );
    }
}