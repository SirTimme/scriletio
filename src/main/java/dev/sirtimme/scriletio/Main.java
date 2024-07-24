package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.factory.event.*;
import dev.sirtimme.scriletio.managers.EventCommandManager;
import dev.sirtimme.scriletio.factory.interaction.ButtonEventCommandFactory;
import dev.sirtimme.scriletio.factory.interaction.CommandAutoCompleteEventCommandFactory;
import dev.sirtimme.scriletio.factory.interaction.MenuEventCommandFactory;
import dev.sirtimme.scriletio.factory.interaction.SlashEventCommandFactory;
import dev.sirtimme.scriletio.managers.DeleteTaskManager;
import dev.sirtimme.scriletio.managers.InteractionCommandManager;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (System.getenv("LOG_EXPORTER_ENDPOINT") != null) {
            OpenTelemetryAppender.install(buildOpenTelemetry());

            LOGGER.info("Initialization of OpenTelemetry successful");
        } else {
            LOGGER.info("Environment variable 'LOG_EXPORTER_ENDPOINT' is not set, skipping initialization of OpenTelemetry");
        }

        final var entityManagerFactory = buildEntityManagerFactory();
        final var deleteTaskManager = new DeleteTaskManager();

        JDABuilder
            .createLight(System.getenv("TOKEN"), GatewayIntent.GUILD_MESSAGES)
            .addEventListeners(
                // normal events
                new EventCommandManager<>(MessageReceivedEvent.class, entityManagerFactory, new MessageReceiveEventCommandFactory(deleteTaskManager)),
                new EventCommandManager<>(MessageDeleteEvent.class, entityManagerFactory, new MessageDeleteEventCommandFactory(deleteTaskManager)),
                new EventCommandManager<>(GuildReadyEvent.class, entityManagerFactory, new GuildReadyEventCommandFactory(deleteTaskManager)),
                new EventCommandManager<>(ChannelDeleteEvent.class, entityManagerFactory, new ChannelDeleteEventCommandFactory()),

                // interaction events
                new InteractionCommandManager<>(SlashCommandInteractionEvent.class, entityManagerFactory, new SlashEventCommandFactory()),
                new InteractionCommandManager<>(ButtonInteractionEvent.class, entityManagerFactory, new ButtonEventCommandFactory()),
                new InteractionCommandManager<>(StringSelectInteractionEvent.class, entityManagerFactory, new MenuEventCommandFactory()),
                new InteractionCommandManager<>(CommandAutoCompleteInteractionEvent.class, entityManagerFactory, new CommandAutoCompleteEventCommandFactory())
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

    private static OpenTelemetry buildOpenTelemetry() {
        final var logRecordExporter = OtlpGrpcLogRecordExporter
            .builder()
            .setEndpoint(System.getenv("LOG_EXPORTER_ENDPOINT"))
            .build();

        final var logRecordProcessor = BatchLogRecordProcessor
            .builder(logRecordExporter)
            .build();

        final var serviceNameResource = Resource
            .getDefault()
            .toBuilder()
            .put("service.name", "scriletio")
            .build();

        final var loggerProvider = SdkLoggerProvider
            .builder()
            .setResource(serviceNameResource)
            .addLogRecordProcessor(logRecordProcessor)
            .build();

        final var openTelemetrySDK = OpenTelemetrySdk
            .builder()
            .setLoggerProvider(loggerProvider)
            .build();

        // Add hook to close SDK, which flushes logs
        Runtime.getRuntime().addShutdownHook(new Thread(openTelemetrySDK::close));

        return openTelemetrySDK;
    }
}