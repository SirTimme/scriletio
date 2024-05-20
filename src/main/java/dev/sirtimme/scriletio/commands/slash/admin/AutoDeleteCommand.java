package dev.sirtimme.scriletio.commands.slash.admin;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.models.Agreement;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.slash.IsAdmin;
import dev.sirtimme.scriletio.preconditions.slash.IsRegistered;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class AutoDeleteCommand implements ISlashCommand {
    private final IRepository<Agreement> agreementRepository;
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public AutoDeleteCommand(final IRepository<Agreement> agreementRepository, final IRepository<DeleteConfig> deleteConfigRepository) {
        this.agreementRepository = agreementRepository;
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var subCommandName = DeleteSubCommand.valueOf(event.getSubcommandName().toUpperCase());
        final var subCommand = switch (subCommandName) {
            case ADD -> new AddConfigCommand(deleteConfigRepository);
            case GET -> new GetConfigCommand(deleteConfigRepository);
            case UPDATE -> new UpdateConfigCommand(deleteConfigRepository);
            case DELETE -> new DeleteConfigCommand(deleteConfigRepository);
        };

        subCommand.execute(event);
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new IsRegistered(agreementRepository),
            new IsAdmin()
        );
    }

    @Override
    public CommandData getCommandData() {
        final var addCommandData = AddConfigCommand.getSubcommandData();
        final var getCommandData = GetConfigCommand.getSubcommandData();
        final var deleteCommandData = DeleteConfigCommand.getSubcommandData();
        final var updateCommandData = UpdateConfigCommand.getSubcommandData();

        return Commands.slash("autodelete", "Manage auto delete configs").addSubcommands(addCommandData, getCommandData, deleteCommandData, updateCommandData);
    }

    private enum DeleteSubCommand {
        ADD,
        GET,
        UPDATE,
        DELETE,
    }
}