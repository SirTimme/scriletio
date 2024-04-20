package dev.sirtimme.scriletio.commands.admin;

import dev.sirtimme.scriletio.error.ExceptionFormatter;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.parse.Parser;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;

public class AutoDeleteCommand extends AdminCommand {
    private final IRepository<User> repository;

    public AutoDeleteCommand(final IRepository<User> repository) {
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
        final var channelOptionData = new OptionData(OptionType.CHANNEL, "channel", "The channel to delete the messages in", true).setChannelTypes(ChannelType.TEXT);
        final var durationOptionData = new OptionData(OptionType.STRING, "duration", "Delete messages after specified duration", true);
        final var addCommandData = new SubcommandData("add", "Adds a new auto delete config").addOptions(channelOptionData, durationOptionData);
        final var getCommandData = new SubcommandData("get", "Displays all of your create auto delete configs");

        return Commands.slash("autodelete", "Manage auto delete configs").addSubcommands(addCommandData, getCommandData);
    }

    private void handleAddCommand(final SlashCommandInteractionEvent event) {
        final var channel = event.getOption("channel").getAsChannel();
        final var durationString = event.getOption("duration").getAsString();

        var duration = 0L;
        try {
            duration = new Parser().parse(durationString);
        } catch (ParsingException exception) {
            event.reply(ExceptionFormatter.format(durationString, exception)).queue();
            return;
        }

        final var deleteConfig = new DeleteConfig(event.getGuild().getIdLong(), channel.getIdLong(), duration);
        final var user = getOrCreateUser(event.getUser().getIdLong());

        user.addConfig(deleteConfig);
        repository.update(user);

        event.reply("Successfully created an auto delete config for **" + channel + "**").queue();
    }

    private void handleGetCommand(final SlashCommandInteractionEvent event) {
        final var userId = event.getUser().getIdLong();

        final var user = repository.get(userId);
        if (user == null) {
            event.reply("You dont have any configs saved").queue();
            return;
        }

        final var amountConfigs = user.getConfigs().size();

        event.reply("You have currently **" + amountConfigs + "** configs saved").queue();
    }

    private void handleUpdateCommand(final SlashCommandInteractionEvent event) {

    }

    private void handleDeleteCommand(final SlashCommandInteractionEvent event) {

    }

    private User getOrCreateUser(final long userId) {
        final var dbUser = repository.get(userId);
        if (dbUser != null) {
            return dbUser;
        }

        final var newUser = new User(userId, new ArrayList<>());
        repository.add(newUser);

        return newUser;
    }

    private enum DeleteSubCommand {
        ADD,
        GET,
        UPDATE,
        DELETE,
    }
}