package dev.sirtimme.scriletio;

import dev.sirtimme.iuvo.api.listener.event.EventListener;
import dev.sirtimme.iuvo.api.listener.interaction.InteractionListener;
import dev.sirtimme.iuvo.api.localization.LocalizationManager;
import dev.sirtimme.scriletio.factory.event.*;
import dev.sirtimme.scriletio.factory.interaction.ButtonEventCommandFactory;
import dev.sirtimme.scriletio.factory.interaction.MenuEventCommandFactory;
import dev.sirtimme.scriletio.factory.interaction.SlashEventCommandFactory;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {
    static void main() {
        final var entityManagerFactory = buildEntityManagerFactory();
        final var localizationManager = new LocalizationManager();
        final var deleteTaskManager = new DeleteTaskManager();

        JDABuilder
            .createLight(System.getenv("BOT_TOKEN"), GatewayIntent.GUILD_MESSAGES)
            .addEventListeners(
                // normal events
                new EventListener<>(MessageReceivedEvent.class, entityManagerFactory, new MessageReceiveEventCommandFactory(deleteTaskManager)),
                new EventListener<>(MessageDeleteEvent.class, entityManagerFactory, new MessageDeleteEventCommandFactory(deleteTaskManager)),
                new EventListener<>(GuildReadyEvent.class, entityManagerFactory, new GuildReadyEventCommandFactory(deleteTaskManager)),
                new EventListener<>(ChannelDeleteEvent.class, entityManagerFactory, new ChannelDeleteEventCommandFactory()),

                // interaction events
                new InteractionListener<>(SlashCommandInteractionEvent.class, entityManagerFactory, new SlashEventCommandFactory(localizationManager)),
                new InteractionListener<>(ButtonInteractionEvent.class, entityManagerFactory, new ButtonEventCommandFactory(localizationManager)),
                new InteractionListener<>(StringSelectInteractionEvent.class, entityManagerFactory, new MenuEventCommandFactory(localizationManager))
            )
            .build();
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