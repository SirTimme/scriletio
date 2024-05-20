package dev.sirtimme.scriletio.factories;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.commands.slash.admin.AutoDeleteCommand;
import dev.sirtimme.scriletio.commands.slash.owner.UpdateCommand;
import dev.sirtimme.scriletio.commands.slash.user.DeleteCommand;
import dev.sirtimme.scriletio.commands.slash.user.RegisterCommand;
import dev.sirtimme.scriletio.repositories.AgreementRepository;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import jakarta.persistence.EntityManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class SlashCommandFactory implements ICommandFactory<SlashCommandInteractionEvent> {
    private final HashMap<String, Function<EntityManager, ISlashCommand>> slashCommands;

    public SlashCommandFactory() {
        this.slashCommands = new HashMap<>();
        this.slashCommands.put("update", context -> new UpdateCommand(this));
        this.slashCommands.put("autodelete", context -> new AutoDeleteCommand(new AgreementRepository(context), new DeleteConfigRepository(context)));
        this.slashCommands.put("register", context -> new RegisterCommand(new AgreementRepository(context)));
        this.slashCommands.put("delete", context -> new DeleteCommand(new AgreementRepository(context)));
    }

    @Override
    public ICommand<SlashCommandInteractionEvent> createCommand(final SlashCommandInteractionEvent event, final EntityManager context) {
        return slashCommands.get(event.getName()).apply(context);
    }

    public List<CommandData> getCommandData() {
        return slashCommands
            .values()
            .stream()
            .map(function -> function.apply(null).getCommandData())
            .toList();
    }
}