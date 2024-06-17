package dev.sirtimme.scriletio.commands.interaction;

import dev.sirtimme.scriletio.commands.ICommand;
import dev.sirtimme.scriletio.precondition.IPreconditionHolder;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface IInteractionCommand<T extends GenericInteractionCreateEvent> extends ICommand<T>, IPreconditionHolder<T> {}
