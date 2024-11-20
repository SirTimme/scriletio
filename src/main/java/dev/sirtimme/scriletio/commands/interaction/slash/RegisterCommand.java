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

import static dev.sirtimme.scriletio.response.Markdown.bold;
import static dev.sirtimme.scriletio.response.Markdown.monospace;

public class RegisterCommand implements ISlashCommand {
    private final Repository<User> userRepository;
    private final LocalizationManager l10nManager;

    public RegisterCommand(final Repository<User> userRepository, final LocalizationManager l10nManager) {
        this.userRepository = userRepository;
        this.l10nManager = l10nManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = userRepository.get(userId);

        if (user != null) {
            event.reply("You are already registered").queue();
            return;
        }

        final var btnAccept = Button.success(userId + ":register-accept", l10nManager.get("button.label.accept"));
        final var btnCancel = Button.danger(userId + ":register-cancel", l10nManager.get("button.label.cancel"));

        final var response = l10nManager.get(
            "slash.register",
            bold(l10nManager.get("notice")),
            monospace(l10nManager.get("delete.name")),
            bold(l10nManager.get("continue"))
        );

        event.reply(response).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(l10nManager.get("register.name", Locale.US), l10nManager.get("register.description", Locale.US));
    }
}