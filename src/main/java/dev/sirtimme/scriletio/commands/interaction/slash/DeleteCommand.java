package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.precondition.IPrecondition;
import dev.sirtimme.scriletio.precondition.interaction.slash.IsRegistered;
import dev.sirtimme.scriletio.repository.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class DeleteCommand implements ISlashCommand {
    private final IRepository<User> userRepository;

    public DeleteCommand(final IRepository<User> userRepository) {
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
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new IsRegistered(userRepository)
        );
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("delete", "Deletes all of your stored data")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Löscht all deine gespeicherten Daten");
    }
}