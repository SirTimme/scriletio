package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import dev.sirtimme.scriletio.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.scriletio.response.Markdown.*;

public class RegisterCommand implements ISlashCommand {
    private final Repository<User> userRepository;
    private final LocalizationManager localizationManager;

    public RegisterCommand(final Repository<User> userRepository, final LocalizationManager localizationManager) {
        this.userRepository = userRepository;
        this.localizationManager = localizationManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = userRepository.get(userId);

        if (user != null) {
            event.reply(localizationManager.get("error.alreadyRegistered")).queue();
            return;
        }

        final var btnAccept = Button.success(userId + ":register-accept", localizationManager.get("button.label.accept"));
        final var btnCancel = Button.danger(userId + ":register-cancel", localizationManager.get("button.label.cancel"));

        final var response = localizationManager.get(
            "slash.register",
            h2(localizationManager.get("notice")),
            command(localizationManager.get("delete.name", Locale.US), 1304427640861622345L),
            h3(localizationManager.get("continue"))
        );

        event.reply(response).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of();
    }

    @Override
    public CommandData getCommandData() {
        final var commandName = localizationManager.get("register.name", Locale.US);
        final var commandDescription = localizationManager.get("register.description", Locale.US);

        return Commands.slash(commandName, commandDescription);
    }
}