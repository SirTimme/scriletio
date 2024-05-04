package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.format.Formatter;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.models.User;
import dev.sirtimme.scriletio.parse.Parser;
import dev.sirtimme.scriletio.preconditions.HasRegistered;
import dev.sirtimme.scriletio.preconditions.IPreconditionCheck;
import dev.sirtimme.scriletio.preconditions.IsAdmin;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class AutoDeleteCommand implements ISlashCommand {
	private final IRepository<User> userRepository;
	private final IRepository<DeleteConfig> deleteConfigRepository;

	public AutoDeleteCommand(final IRepository<User> userRepository, final IRepository<DeleteConfig> deleteConfigRepository) {
		this.userRepository = userRepository;
		this.deleteConfigRepository = deleteConfigRepository;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event) {
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
		final var channelOptionData = new OptionData(OptionType.CHANNEL, "channel", "The channel to delete the messages in", true)
				.setChannelTypes(ChannelType.TEXT);

		final var durationOptionData = new OptionData(OptionType.STRING, "duration", "Delete messages after specified duration", true);
		final var deleteChannelData = new OptionData(OptionType.CHANNEL, "channel", "Directly specify a channel to delete a config for", false)
				.setChannelTypes(ChannelType.TEXT);

		final var addCommandData = new SubcommandData("add", "Adds a new auto delete config").addOptions(channelOptionData, durationOptionData);
		final var getCommandData = new SubcommandData("get", "Displays all of your create auto delete configs");
		final var deleteCommandData = new SubcommandData("delete", "Deletes an existing auto delete config").addOptions(deleteChannelData);
		final var updateCommandData = new SubcommandData("update", "Updates an existing auto delete config");

		return Commands.slash("autodelete", "Manage auto delete configs")
					   .addSubcommands(addCommandData, getCommandData, deleteCommandData, updateCommandData);
	}

	@Override
	public List<IPreconditionCheck> getPreconditions() {
		return List.of(
				new HasRegistered(userRepository),
				new IsAdmin()
		);
	}

	private void handleAddCommand(final SlashCommandInteractionEvent event) {
		final var user = userRepository.get(event.getUser().getIdLong());
		if (user == null) {
			event.reply("You are not registered, please use `/register` first").queue();
			return;
		}

		if (user.getConfigs().size() == 25) {
			event.reply("Due to discords limitations, you cannot have more than **25** auto delete configs").queue();
			return;
		}

		final var durationString = event.getOption("duration").getAsString();
		var duration = 0L;
		try {
			duration = new Parser().parse(durationString);
		} catch (ParsingException exception) {
			event.reply(Formatter.format(durationString, exception)).queue();
			return;
		}

		final var channel = event.getOption("channel").getAsChannel();
		final var config = deleteConfigRepository.get(channel.getIdLong());
		if (config != null) {
			event.reply("There is already a delete config for that channel!").queue();
			return;
		}

		user.addConfig(new DeleteConfig(user, event.getGuild().getIdLong(), channel.getIdLong(), duration));

		event.reply("Successfully created an auto delete config for **" + channel + "**").queue();
	}

	private void handleGetCommand(final SlashCommandInteractionEvent event) {
		final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());
		final var sb = new StringBuilder();
		sb.append("**Saved configs for ").append(event.getGuild().getName()).append(":**\n\n");
		for (var config : deleteConfigs) {
			sb.append("- <#").append(config.getChannelId()).append("> ").append(config.getDuration()).append(" minutes\n");
		}

		event.reply(sb.toString()).queue();
	}

	private void handleUpdateCommand(final SlashCommandInteractionEvent event) {
		final var selectMenu = buildConfigSelectMenu(event, "update");
		if (selectMenu == null) {
			return;
		}

		event.reply("Please select the config you want to update").addActionRow(selectMenu).queue();
	}

	private void handleDeleteCommand(final SlashCommandInteractionEvent event) {
		final var channelOption = event.getOption("channel");
		if (channelOption != null) {
			final var selectedChannel = channelOption.getAsChannel();
			final var deleteConfig = deleteConfigRepository.get(selectedChannel.getIdLong());
			if (deleteConfig != null) {
				final var author = deleteConfig.getUser();

				author.removeConfig(selectedChannel.getIdLong());

				event.reply("Config for channel " + selectedChannel.getAsMention() + " has been deleted").queue();
				return;
			}
		}

		final var selectMenu = buildConfigSelectMenu(event, "delete");
		if (selectMenu == null) {
			return;
		}

		event.reply("Please select the config you want to delete").addActionRow(selectMenu).queue();
	}

	private StringSelectMenu buildConfigSelectMenu(final SlashCommandInteractionEvent event, final String menuId) {
		final var userId = event.getUser().getIdLong();
		final var user = userRepository.get(userId);
		if (user == null) {
			event.reply("You are not registered, please use `/register` first").queue();
			return null;
		}

		final var userConfigs = user.getConfigs();
		if (userConfigs.isEmpty()) {
			event.reply("You don't have any configs saved").queue();
			return null;
		}

		final var menuBuilder = StringSelectMenu.create(userId + ":" + menuId).setPlaceholder("Saved configs");

		for (var config : userConfigs) {
			final var channel = event.getGuild().getChannelById(TextChannel.class, config.getChannelId());
			final var label = "#" + channel.getName();
			final var value = String.valueOf(config.getChannelId());
			final var description = createReadableDuration(config.getDuration());

			menuBuilder.addOption(label, value, description, Emoji.fromUnicode("U+1F4D1"));
		}

		return menuBuilder.build();
	}

	private String createReadableDuration(final long minutes) {
		final var days = minutes / (24 * 60);
		final var hours = (minutes % (24 * 60)) / 60;
		final var remaining = minutes % 60;

		return "Duration: " + days + " days, " + hours + " hours, " + remaining + " minutes";
	}

	private enum DeleteSubCommand {
		ADD,
		GET,
		UPDATE,
		DELETE,
	}
}