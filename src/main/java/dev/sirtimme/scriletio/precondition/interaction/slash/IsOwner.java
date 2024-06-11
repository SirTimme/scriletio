package dev.sirtimme.scriletio.precondition.interaction.slash;

import dev.sirtimme.scriletio.precondition.IPrecondition;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class IsOwner implements IPrecondition<SlashCommandInteractionEvent> {
    @Override
    public boolean isValid(final SlashCommandInteractionEvent event) {
        if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
            event.reply("This command can only be executed by the owner").setEphemeral(true).queue();
            return false;
        }

        return true;
    }
}