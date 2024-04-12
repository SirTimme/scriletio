package dev.sirtimme.scriletio.commands.admin;

import dev.sirtimme.scriletio.error.ExceptionFormatter;
import dev.sirtimme.scriletio.error.ParsingException;
import dev.sirtimme.scriletio.parse.Parser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AutoDeleteCommand extends AdminCommand {
    @Override
    protected void handleCommand(final SlashCommandInteractionEvent event) {
        final var channel = event.getOption("channel").getAsChannel();
        final var durationString = event.getOption("duration").getAsString();

        var duration = 0L;
        try {
            duration = new Parser().parse(durationString);
        } catch (ParsingException exception) {
            event.reply(ExceptionFormatter.format(durationString, exception)).queue();
        }

        event.reply("You want to delete messages in **" + channel + "** after **" + duration + "** minutes").queue();
    }

    @Override
    public CommandData getCommandData() {
        final var addCommandData = new SubcommandData("add", "Adds a new auto delete config")
                .addOption(OptionType.CHANNEL, "channel", "The channel to delete the messages in", true)
                .addOption(OptionType.STRING, "duration", "Delete messages after specified duration", true);

        return Commands.slash("autodelete", "Manage auto delete configs")
                       .addSubcommands(addCommandData);
    }
}