package dev.sirtimme.scriletio.components.buttons.register;

import dev.sirtimme.scriletio.components.buttons.MessageButton;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.ArrayList;
import java.util.Collections;

public class RegisterAccept extends MessageButton {
    private final IRepository<User> repository;

    public RegisterAccept(final IRepository<User> repository) {
        this.repository = repository;
    }

    @Override
    protected void handleCommand(final ButtonInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = new User(userId, new ArrayList<>());

        repository.add(user);

        event.editMessage("You were successfully registered").setComponents(Collections.emptyList()).queue();
    }
}