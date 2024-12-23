package dev.sirtimme.scriletio.commands.interaction.sub;

import dev.sirtimme.iuvo.api.commands.interaction.ISubCommand;
import dev.sirtimme.iuvo.api.precondition.IPrecondition;
import dev.sirtimme.iuvo.api.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import dev.sirtimme.scriletio.localization.LocalizationManager;
import dev.sirtimme.scriletio.precondition.HasSavedConfigs;
import dev.sirtimme.scriletio.utils.TimeUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;
import java.util.Locale;

public class GetConfigCommand implements ISubCommand {
    private final QueryableRepository<DeleteConfig> configRepository;
    private final LocalizationManager localizationManager;

    public GetConfigCommand(final QueryableRepository<DeleteConfig> configRepository, final LocalizationManager localizationManager) {
        this.configRepository = configRepository;
        this.localizationManager = localizationManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        // noinspection DataFlowIssue command can only be executed within a guild
        final var deleteConfigs = configRepository.findAll(event.getGuild().getIdLong());
        final var response = createResponse(localizationManager, deleteConfigs);

        event.reply(response).queue();
    }

    @Override
    public List<IPrecondition<? super SlashCommandInteractionEvent>> getPreconditions() {
        return List.of(
            new HasSavedConfigs(configRepository, localizationManager)
        );
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(localizationManager.get("auto-delete.get.name", Locale.US), localizationManager.get("auto-delete.get.description", Locale.US));
    }

    private static String createResponse(final LocalizationManager localizationManager, final List<DeleteConfig> deleteConfigs) {
        final var sb = new StringBuilder();
        final var channel = localizationManager.get("channel");

        for (final var deleteConfig : deleteConfigs) {
            sb.append(channel)
              .append(": <#")
              .append(deleteConfig.getChannelId())
              .append(">")
              .append("\n")
              .append(TimeUtils.createReadableDuration(deleteConfig.getDuration()))
              .append("\n\n");
        }

        return sb.toString();
    }
}