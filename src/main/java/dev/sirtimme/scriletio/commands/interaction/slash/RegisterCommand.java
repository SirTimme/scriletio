package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.precondition.IPrecondition;
import dev.sirtimme.iuvo.repository.Repository;
import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;
import java.util.Locale;

public class RegisterCommand implements ISlashCommand {
    private final Repository<User> userRepository;

    public RegisterCommand(final Repository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        final var userId = event.getUser().getIdLong();
        final var user = userRepository.get(userId);

        if (user != null) {
            event.reply("You are already registered").queue();
            return;
        }

        final var btnAccept = Button.success(userId + ":registerAccept", "Accept");
        final var btnCancel = Button.danger(userId + ":registerCancel", "Cancel");

        event.reply(Formatter.format()).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("register", "Register yourself to use Scriletios services");
    }
}