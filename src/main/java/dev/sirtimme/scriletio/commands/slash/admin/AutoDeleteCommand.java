package dev.sirtimme.scriletio.commands.slash.admin;

import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.format.Formatter;
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

	private void handleAddCommand(final SlashCommandInteractionEvent event) {
		final var durationString = event.getOption("duration").getAsString();

		var duration = 0L;
		try {
			duration = new Parser().parse(durationString);
		} catch (ParsingException exception) {
			event.reply(Formatter.format(durationString, exception)).queue();
			return;
		}

		final var channel = event.getOption("channel").getAsChannel();
		final var deleteConfig = new DeleteConfig(channel.getIdLong(), duration);

		final var user = repository.get(event.getUser().getIdLong());
		if (user == null) {
			event.reply("You are not registered, please use `/register` first").queue();
			return;
		}

		user.addConfig(deleteConfig);
		repository.update(user);

		event.reply("Successfully created an auto delete config for **" + channel + "**").queue();
	}

	private void handleGetCommand(final SlashCommandInteractionEvent event) {
		final var userId = event.getUser().getIdLong();
		final var user = repository.get(userId);
		if (user == null) {
			event.reply("You are not registered, please use `/register` first").queue();
			return;
		}

		final var amountConfigs = user.getConfigs().size();

		event.reply("You have currently **" + amountConfigs + "** configs saved").queue();
	}

	private void handleUpdateCommand(final SlashCommandInteractionEvent event) {

	}

	private void handleDeleteCommand(final SlashCommandInteractionEvent event) {

	}

	@Override
	public CommandData getCommandData() {
		final var channelOptionData = new OptionData(OptionType.CHANNEL, "channel", "The channel to delete the messages in", true).setChannelTypes(ChannelType.TEXT);
		final var durationOptionData = new OptionData(OptionType.STRING, "duration", "Delete messages after specified duration", true);

		final var addCommandData = new SubcommandData("add", "Adds a new auto delete config").addOptions(channelOptionData, durationOptionData);
		final var getCommandData = new SubcommandData("get", "Displays all of your create auto delete configs");
		final var deleteCommandData = new SubcommandData("delete", "Deletes an existing auto delete config");
		final var updateCommandData = new SubcommandData("update", "Updates an existing auto delete config");

		return Commands.slash("autodelete", "Manage auto delete configs")
					   .addSubcommands(addCommandData, getCommandData, deleteCommandData, updateCommandData);
	}

	private enum DeleteSubCommand {
		ADD,
		GET,
		UPDATE,
		DELETE,
	}
}