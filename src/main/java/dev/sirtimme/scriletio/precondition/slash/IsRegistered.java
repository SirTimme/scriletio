package dev.sirtimme.scriletio.precondition.slash;

import dev.sirtimme.iuvo.precondition.IPrecondition;
import dev.sirtimme.iuvo.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsRegistered implements IPrecondition<SlashCommandInteractionEvent> {
    private final Repository<User> userRepository;

    public IsRegistered(final Repository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        final var user = userRepository.get(event.getUser().getIdLong());

        if (user == null) {
            event.reply("You are not registered, please use `/register` first").queue();
            return false;
        }

        return true;
    }
}