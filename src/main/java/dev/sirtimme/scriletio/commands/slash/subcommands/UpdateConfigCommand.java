package dev.sirtimme.scriletio.commands.slash.subcommands;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.models.DeleteConfig;
import dev.sirtimme.scriletio.preconditions.IPrecondition;
import dev.sirtimme.scriletio.preconditions.slash.HasAtLeast1Config;
import dev.sirtimme.scriletio.repositories.IRepository;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

import static dev.sirtimme.scriletio.time.TimeUtils.createReadableDuration;

public class UpdateConfigCommand implements ICommand<SlashCommandInteractionEvent> {
    private final IRepository<DeleteConfig> deleteConfigRepository;

    public UpdateConfigCommand(final IRepository<DeleteConfig> deleteConfigRepository) {
        this.deleteConfigRepository = deleteConfigRepository;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var deleteConfigs = deleteConfigRepository.findAll(event.getGuild().getIdLong());
        final var updateMenuBuilder = StringSelectMenu.create(event.getUser().getIdLong() + ":" + "update").setPlaceholder("Saved configs");

        for (final var config : deleteConfigs) {
            final var channel = event.getGuild().getChannelById(TextChannel.class, config.getChannelId());
            final var channelName = "#" + channel.getName();
            final var value = String.valueOf(config.getChannelId());
            final var description = createReadableDuration(config.getDuration());

            updateMenuBuilder.addOption(channelName, value, description, Emoji.fromUnicode("U+1F4D1"));
        }

        event.reply("Please select the config you want to update").addActionRow(updateMenuBuilder.build()).queue();
    }

    @Override
    public List<IPrecondition<SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasAtLeast1Config(deleteConfigRepository)
        );
    }

    public static SubcommandData getSubcommandData() {
        return new SubcommandData("update", "Updates an existing auto delete config");
    }
}