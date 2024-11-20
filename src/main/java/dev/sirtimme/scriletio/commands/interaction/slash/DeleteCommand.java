package dev.sirtimme.scriletio.commands.interaction.slash;

import dev.sirtimme.iuvo.api.commands.interaction.ISlashCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;
import java.util.Locale;

import static dev.sirtimme.iuvo.api.precondition.IPrecondition.isRegistered;

public class DeleteCommand implements ISlashCommand {
    private final Repository<User> userRepository;
    private final LocalizationManager l10nManager;

    public DeleteCommand(final Repository<User> userRepository, final LocalizationManager l10nManager) {
        this.userRepository = userRepository;
        this.l10nManager = l10nManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var btnAccept = Button.success(userId + ":delete-accept", l10nManager.get("button.label.accept"));
        final var btnCancel = Button.danger(userId + ":delete-cancel", l10nManager.get("button.label.cancel"));

        event.reply(l10nManager.get("slash.delete")).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            isRegistered(userRepository)
        );
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(l10nManager.get("delete.name", Locale.US), l10nManager.get("delete.description", Locale.US));
    }
}