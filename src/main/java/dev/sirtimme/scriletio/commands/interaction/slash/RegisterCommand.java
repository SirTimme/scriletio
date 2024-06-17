package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.scriletio.utils.Formatter;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.slash.IsNotRegistered;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class RegisterCommand implements ISlashCommand {
    private final IRepository<User> userRepository;

    public RegisterCommand(final IRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var btnAccept = Button.success(userId + ":registerAccept", "Accept");
        final var btnCancel = Button.danger(userId + ":registerCancel", "Cancel");

        event.reply(Formatter.format()).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new IsNotRegistered(userRepository)
        );
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("register", "Register yourself to use Scriletios services");
    }
}