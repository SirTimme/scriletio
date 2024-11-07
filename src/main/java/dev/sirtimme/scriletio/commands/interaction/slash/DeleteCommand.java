package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class DeleteCommand implements ISlashCommand {
    private final Repository<User> userRepository;

    public DeleteCommand(final Repository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var btnAccept = Button.success(userId + ":deleteAccept", "Accept");
        final var btnCancel = Button.danger(userId + ":deleteCancel", "Cancel");

        event.reply("Do you really want to delete your data? All of your created delete configs will be permanently deleted").addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            IPrecondition.isRegistered(userRepository)
        );
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("delete", "Deletes all of your stored data");
    }
}