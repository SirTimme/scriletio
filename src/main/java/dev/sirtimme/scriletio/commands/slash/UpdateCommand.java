package dev.sirtimme.scriletio.commands.slash;

import dev.sirtimme.scriletio.commands.CommandManager;
import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.preconditions.IPreconditionCheck;
import dev.sirtimme.scriletio.preconditions.IsOwner;
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
	public List<IPreconditionCheck> getPreconditions() {
		return List.of(
				new IsOwner()
		);
	}
}