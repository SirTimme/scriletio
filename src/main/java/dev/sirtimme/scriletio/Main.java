package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.factory.event.ChannelDeleteEventCommandFactory;
import dev.sirtimme.scriletio.factory.event.GuildReadyEventCommandFactory;
import dev.sirtimme.scriletio.factory.event.MessageDeleteEventCommandFactory;
import dev.sirtimme.scriletio.factory.event.MessageReceiveEventCommandFactory;
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
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        final var openTelemetrySdk = initializeOpenTelemetry();
        OpenTelemetryAppender.install(openTelemetrySdk);

        final var eventHandler = buildEventhandler();

        JDABuilder.createLight(System.getenv("TOKEN"), GatewayIntent.GUILD_MESSAGES)
                  .addEventListeners(eventHandler)
                  .build();
    }

    private static EventHandler buildEventhandler() {
        final var entityManagerFactory = buildEntityManagerFactory();
        final var deleteTaskManager = new DeleteTaskManager();

        // interaction events
        final var slashCommandManager = new InteractionCommandManager<>(entityManagerFactory, new SlashEventCommandFactory());
        final var buttonCommandManager = new InteractionCommandManager<>(entityManagerFactory, new ButtonEventCommandFactory());
        final var menuCommandManager = new InteractionCommandManager<>(entityManagerFactory, new MenuEventCommandFactory());
        final var commandAutoCompleteCommandManager = new InteractionCommandManager<>(entityManagerFactory, new CommandAutoCompleteEventCommandFactory());

        // normal events
        final var messageReceiveManager = new EventCommandManager<>(entityManagerFactory, new MessageReceiveEventCommandFactory(deleteTaskManager));
        final var messageDeleteManager = new EventCommandManager<>(entityManagerFactory, new MessageDeleteEventCommandFactory(deleteTaskManager));
        final var guildReadyCommandManager = new EventCommandManager<>(entityManagerFactory, new GuildReadyEventCommandFactory(deleteTaskManager));
        final var channelDeleteCommandManager = new EventCommandManager<>(entityManagerFactory, new ChannelDeleteEventCommandFactory());

        return new EventHandler(
            slashCommandManager,
            buttonCommandManager,
            messageReceiveManager,
            messageDeleteManager,
            menuCommandManager,
            guildReadyCommandManager,
            channelDeleteCommandManager,
            commandAutoCompleteCommandManager
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

    private static OpenTelemetry initializeOpenTelemetry() {
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