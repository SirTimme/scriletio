package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.events.EventHandler;
import dev.sirtimme.scriletio.factories.*;
import dev.sirtimme.scriletio.managers.CommandManager;
import dev.sirtimme.scriletio.managers.DeleteJobManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        final var eventHandler = buildEventhandler();

        JDABuilder.createLight(System.getenv("TOKEN"), GatewayIntent.GUILD_MESSAGES)
                  .addEventListeners(eventHandler)
                  .setActivity(Activity.playing("Silentium"))
                  .build();
    }

    private static EventHandler buildEventhandler() {
        final var entityManagerFactory = buildEntityManagerFactory();
        final var deleteJobManager = new DeleteJobManager();

        final var slashCommandManager = new CommandManager<>(entityManagerFactory, new SlashCommandFactory());
        final var buttonCommandManager = new CommandManager<>(entityManagerFactory, new ButtonCommandFactory());
        final var messageReceiveManager = new CommandManager<>(entityManagerFactory, new ReceiveMessageCommandFactory(deleteJobManager));
        final var messageDeleteManager = new CommandManager<>(entityManagerFactory, new DeleteMessageCommandFactory(deleteJobManager));
        final var menuCommandManager = new CommandManager<>(entityManagerFactory, new MenuCommandFactory());
        final var modalCommandManager = new CommandManager<>(entityManagerFactory, new ModalCommandFactory());

        return new EventHandler(
            slashCommandManager,
            buttonCommandManager,
            messageReceiveManager,
            messageDeleteManager,
            menuCommandManager,
            modalCommandManager
        );
    }

    private static EntityManagerFactory buildEntityManagerFactory() {
        final var properties = new HashMap<String, String>() {{
            put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
            put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
            put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
        }};

        return Persistence.createEntityManagerFactory("scriletio", properties);
    }
}