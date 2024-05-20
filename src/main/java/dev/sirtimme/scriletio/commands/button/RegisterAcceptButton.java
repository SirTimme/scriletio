package dev.sirtimme.scriletio.commands.button;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.models.Agreement;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.button.IsButtonAuthor;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.List;

public class RegisterAcceptButton implements ICommand<ButtonInteractionEvent> {
    private final IRepository<Agreement> agreementRepository;

    public RegisterAcceptButton(final IRepository<Agreement> agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    @Override
    public void execute(final ButtonInteractionEvent event) {
        final var userId = event.getUser().getIdLong();

        agreementRepository.add(new Agreement(userId));

        event.editMessage("You were successfully registered").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<ButtonInteractionEvent>> getPreconditions() {
        return List.of(
            new IsButtonAuthor()
        );
    }
}