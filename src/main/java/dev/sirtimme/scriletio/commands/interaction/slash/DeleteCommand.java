package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isRegistered;

public class DeleteCommand implements ISlashCommand {
    private final Repository<User> userRepository;
    private final LocalizationManager localizationManager;

    public DeleteCommand(final Repository<User> userRepository, final LocalizationManager localizationManager) {
        this.userRepository = userRepository;
        this.localizationManager = localizationManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var btnAccept = Button.success(userId + ":delete-accept", localizationManager.get("button.label.accept"));
        final var btnCancel = Button.danger(userId + ":delete-cancel", localizationManager.get("button.label.cancel"));

        event.reply(localizationManager.get("slash.delete")).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            isRegistered(userRepository, localizationManager)
        );
    }

    @Override
    public CommandData getCommandData() {
        final var commandName = localizationManager.get("delete.name", Locale.US);
        final var commandDescription = localizationManager.get("delete.description", Locale.US);

        return Commands.slash(commandName, commandDescription);
    }
}