package dev.sirtimme.scriletio.commands.menu;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.menu.IsMenuAuthor;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;
import java.util.List;

public class DeleteMenu implements ICommand<StringSelectInteractionEvent> {
    private final IRepository<User> repository;

    public DeleteMenu(final IRepository<User> repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final StringSelectInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = repository.get(userId);
        final var channelId = event.getValues().getFirst();

        user.removeConfig(Long.parseLong(channelId));

        event.editMessage("Config for channel <#" + channelId + "> was successfully deleted").setComponents(Collections.emptyList()).queue();
    }

    @Override
    public List<IPrecondition<StringSelectInteractionEvent>> getPreconditions() {
        return List.of(
            new IsMenuAuthor()
        );
    }
}