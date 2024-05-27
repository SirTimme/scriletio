package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.models.Agreement;
import dev.sirtimme.scriletio.models.DeleteConfig;
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
    private final IRepository<DeleteConfig> configRepository;

    public DeleteCommand(final IRepository<Agreement> agreementRepository, final IRepository<DeleteConfig> configRepository) {
        this.agreementRepository = agreementRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();
        final var user = agreementRepository.get(userId);

        agreementRepository.delete(user);
        configRepository.deleteAll(userId);

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