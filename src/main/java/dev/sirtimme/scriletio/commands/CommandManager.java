package dev.sirtimme.scriletio.commands;

import dev.sirtimme.scriletio.commands.slash.AutoDeleteCommand;
import dev.sirtimme.scriletio.commands.slash.DeleteCommand;
import dev.sirtimme.scriletio.commands.slash.RegisterCommand;
import dev.sirtimme.scriletio.commands.slash.UpdateCommand;
import dev.sirtimme.scriletio.preconditions.PreconditionResult;
import dev.sirtimme.scriletio.repositories.DeleteConfigRepository;
import dev.sirtimme.scriletio.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class CommandManager {
	private final EntityManagerFactory entityManagerFactory;
	private final HashMap<String, Function<EntityManager, ISlashCommand>> commands;

	public CommandManager(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;

		this.commands = new HashMap<>();
		this.commands.put("update", entityManager -> new UpdateCommand(this));
		this.commands.put("autodelete", entityManager -> new AutoDeleteCommand(new UserRepository(entityManager), new DeleteConfigRepository(entityManager)));
		this.commands.put("register", entityManager -> new RegisterCommand(new UserRepository(entityManager)));
		this.commands.put("delete", entityManager -> new DeleteCommand(new UserRepository(entityManager)));
	}

	public void handleCommand(final SlashCommandInteractionEvent event) {
		final var function = commands.get(event.getName());
		final var entityManager = entityManagerFactory.createEntityManager();
		final var command = function.apply(entityManager);

		if (command.verifyPreconditions(event) == PreconditionResult.FAILURE) {
			return;
		}

		entityManager.getTransaction().begin();
		command.execute(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public List<CommandData> getCommandData() {
		return commands
				.values()
				.stream()
				.map(function -> function.apply(null).getCommandData())
				.toList();
	}
}