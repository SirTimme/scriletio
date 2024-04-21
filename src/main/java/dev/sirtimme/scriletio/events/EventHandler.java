package dev.sirtimme.scriletio.events;

import dev.sirtimme.scriletio.commands.CommandManager;
import dev.sirtimme.scriletio.components.buttons.ButtonManager;
import dev.sirtimme.scriletio.messages.MessageManager;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EventHandler extends ListenerAdapter {
    private final CommandManager commandManager;
    private final ButtonManager buttonManager;
    private final MessageManager messageManager;

    public EventHandler() {
        final var properties = new HashMap<String, String>() {{
            put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
            put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
            put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
        }};
        final var entityManagerFactory = Persistence.createEntityManagerFactory("scriletio", properties);
        final var userRepository = new UserRepository(entityManagerFactory);
        final var deleteConfigRepository = new DeleteConfigRepository(entityManagerFactory);

        this.commandManager = new CommandManager(userRepository);
        this.buttonManager = new ButtonManager(userRepository);
        this.messageManager = new MessageManager(deleteConfigRepository);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        this.commandManager.handleCommand(event);
    }

    @Override
    public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
        this.buttonManager.handleCommand(event);
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        this.messageManager.handleMessage(event);
    }
}