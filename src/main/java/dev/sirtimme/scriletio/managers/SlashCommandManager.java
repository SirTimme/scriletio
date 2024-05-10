package dev.sirtimme.scriletio.managers;

import dev.sirtimme.scriletio.commands.ISlashCommand;
import dev.sirtimme.scriletio.commands.slash.AutoDeleteCommand;
import dev.sirtimme.scriletio.commands.slash.DeleteCommand;
import dev.sirtimme.scriletio.commands.slash.RegisterCommand;
import dev.sirtimme.scriletio.commands.slash.UpdateCommand;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class SlashCommandManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, ISlashCommand>> slashCommands;

	public SlashCommandManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.slashCommands = new HashMap<>();
		this.slashCommands.put("update", entityManager -> new UpdateCommand(this));
		this.slashCommands.put("autodelete", entityManager -> new AutoDeleteCommand(new UserRepository(entityManager), new DeleteConfigRepository(entityManager)));
		this.slashCommands.put("register", entityManager -> new RegisterCommand(new UserRepository(entityManager)));
		this.slashCommands.put("delete", entityManager -> new DeleteCommand(new UserRepository(entityManager)));
	}

	public void handleCommand(final SlashCommandInteractionEvent event) {
		try (final var context = entityManagerFactory.createEntityManager()) {
			final var slashCommand = slashCommands.get(event.getName()).apply(context);

			if (slashCommand.hasInvalidPreconditions(event)) {
				return;
			}

			context.getTransaction().begin();
			slashCommand.execute(event);
			context.getTransaction().commit();
		}
	}

	public List<CommandData> getCommandData() {
		return slashCommands
				.values()
				.stream()
				.map(function -> function.apply(null).getCommandData())
				.toList();
	}
}