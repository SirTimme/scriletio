package dev.sirtimme.scriletio.commands.admin;

import dev.sirtimme.scriletio.error.ExceptionFormatter;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.parse.Parser;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AutoDeleteCommand extends AdminCommand {
    private final IRepository<DeleteConfig> repository;

    public AutoDeleteCommand(final IRepository<DeleteConfig> repository) {
        this.repository = repository;
    }

    @Override
    protected void handleCommand(final SlashCommandInteractionEvent event) {
        final var subCommand = DeleteSubCommand.valueOf(event.getSubcommandName().toUpperCase());
        switch (subCommand) {
            case ADD -> handleAddCommand(event);
            case GET -> handleGetCommand(event);
            case UPDATE -> handleUpdateCommand(event);
            case DELETE -> handleDeleteCommand(event);
        }
    }

    @Override
    public CommandData getCommandData() {
        final var addCommandData = new SubcommandData("add", "Adds a new auto delete config")
                .addOption(OptionType.CHANNEL, "channel", "The channel to delete the messages in", true)
                .addOption(OptionType.STRING, "duration", "Delete messages after specified duration", true);

        final var getCommandData = new SubcommandData("get", "Displays all of your create auto delete configs");

        return Commands.slash("autodelete", "Manage auto delete configs")
                       .addSubcommands(addCommandData, getCommandData);
    }

    private void handleAddCommand(final SlashCommandInteractionEvent event) {
        final var channel = event.getOption("channel").getAsChannel();
        final var durationString = event.getOption("duration").getAsString();
        var duration = 0L;
        try {
            duration = new Parser().parse(durationString);
        } catch (ParsingException exception) {
            event.reply(ExceptionFormatter.format(durationString, exception)).queue();
        }
        final var deleteConfig = new DeleteConfig(event.getUser().getIdLong(), event.getGuild().getIdLong(), channel.getIdLong(), duration);
        repository.add(deleteConfig);
        event.reply("Successfully created an auto delete config for **" + channel + "**").queue();
    }

    private void handleGetCommand(final SlashCommandInteractionEvent event) {
        final var authorId = event.getUser().getIdLong();
        final var result = repository.get(authorId);
        event.reply("These are your current created configs **" + result + "**").queue();
    }

    private void handleUpdateCommand(final SlashCommandInteractionEvent event) {

    }

    private void handleDeleteCommand(final SlashCommandInteractionEvent event) {

    }

    private enum DeleteSubCommand {
        ADD,
        GET,
        UPDATE,
        DELETE;
    }
}