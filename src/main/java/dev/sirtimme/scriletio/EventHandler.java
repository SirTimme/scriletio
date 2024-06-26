package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.managers.ICommandManager;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final ICommandManager<SlashCommandInteractionEvent> slashCommandManager;
    private final ICommandManager<ButtonInteractionEvent> buttonCommandManager;
    private final ICommandManager<MessageReceivedEvent> receiveMessageCommandManager;
    private final ICommandManager<MessageDeleteEvent> deleteMessageCommandManager;
    private final ICommandManager<StringSelectInteractionEvent> menuCommandManager;
    private final ICommandManager<GuildReadyEvent> guildReadyCommandManager;
    private final ICommandManager<ChannelDeleteEvent> channelDeleteCommandManager;
    private final ICommandManager<CommandAutoCompleteInteractionEvent> commandAutoCompleteCommandManager;

    public EventHandler(
        final ICommandManager<SlashCommandInteractionEvent> slashCommandManager,
        final ICommandManager<ButtonInteractionEvent> buttonCommandManager,
        final ICommandManager<MessageReceivedEvent> receiveMessageCommandManager,
        final ICommandManager<MessageDeleteEvent> deleteMessageCommandManager,
        final ICommandManager<StringSelectInteractionEvent> menuCommandManager,
        final ICommandManager<GuildReadyEvent> guildReadyCommandManager,
        final ICommandManager<ChannelDeleteEvent> channelDeleteCommandManager,
        final ICommandManager<CommandAutoCompleteInteractionEvent> commandAutoCompleteCommandManager
    ) {
        this.slashCommandManager = slashCommandManager;
        this.buttonCommandManager = buttonCommandManager;
        this.receiveMessageCommandManager = receiveMessageCommandManager;
        this.deleteMessageCommandManager = deleteMessageCommandManager;
        this.menuCommandManager = menuCommandManager;
        this.guildReadyCommandManager = guildReadyCommandManager;
        this.channelDeleteCommandManager = channelDeleteCommandManager;
        this.commandAutoCompleteCommandManager = commandAutoCompleteCommandManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        slashCommandManager.handleCommand(event);
    }

    @Override
    public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
        buttonCommandManager.handleCommand(event);
    }

    @Override
    public void onStringSelectInteraction(@NotNull final StringSelectInteractionEvent event) {
        menuCommandManager.handleCommand(event);
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        receiveMessageCommandManager.handleCommand(event);
    }

    @Override
    public void onMessageDelete(@NotNull final MessageDeleteEvent event) {
        deleteMessageCommandManager.handleCommand(event);
    }

    @Override
    public void onGuildReady(@NotNull final GuildReadyEvent event) {
        guildReadyCommandManager.handleCommand(event);
    }

    @Override
    public void onChannelDelete(@NotNull final ChannelDeleteEvent event) {
        channelDeleteCommandManager.handleCommand(event);
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull final CommandAutoCompleteInteractionEvent event) {
        commandAutoCompleteCommandManager.handleCommand(event);
    }
}