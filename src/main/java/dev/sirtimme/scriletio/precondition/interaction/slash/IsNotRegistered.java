package dev.sirtimme.scriletio.precondition.interaction.slash;

import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsNotRegistered implements IPrecondition<SlashCommandInteractionEvent> {
    private final IRepository<User> userRepository;

    public IsNotRegistered(final IRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        final var user = userRepository.get(event.getUser().getIdLong());

        if (user != null) {
            event.reply("You are already registered").queue();
            return false;
        }

        return true;
    }
}