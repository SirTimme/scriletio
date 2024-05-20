package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.models.Agreement;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.slash.IsRegistered;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class DeleteCommand implements ISlashCommand {
    private final IRepository<Agreement> agreementRepository;

    public DeleteCommand(final IRepository<Agreement> agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var user = agreementRepository.get(event.getUser().getIdLong());

        agreementRepository.delete(user);

        event.reply("All of your stored data is gone").queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new IsRegistered(agreementRepository)
        );
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("delete", "Deletes all of your stored data")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Löscht all deine gespeicherten Daten");
    }
}