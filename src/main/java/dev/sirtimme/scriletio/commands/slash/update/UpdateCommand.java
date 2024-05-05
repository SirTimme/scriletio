package dev.sirtimme.scriletio.commands.slash.update;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.commands.slash.CommandManager;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.slash.IsOwner;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class UpdateCommand implements ISlashCommand {
	private final CommandManager manager;

	public UpdateCommand(final CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event) {
		event.getJDA()
			 .updateCommands()
			 .addCommands(manager.getCommandData())
			 .queue();

		event.reply("Update of slash commands were successful!").queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("update", "Refreshes all slash commands")
					   .setDescriptionLocalization(DiscordLocale.GERMAN, "Aktualisiert alle Befehle")
					   .setGuildOnly(true)
					   .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
	}

	@Override
	public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
		return List.of(
				new IsOwner()
		);
	}
}