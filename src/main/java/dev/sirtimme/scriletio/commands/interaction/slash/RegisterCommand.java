package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.localization.LocalizationManager;
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

        final var btnAccept = Button.success(userId + ":register-accept", "Accept");
        final var btnCancel = Button.danger(userId + ":register-cancel", "Cancel");

        event.reply(Formatter.format()).addActionRow(btnAccept, btnCancel).queue();
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