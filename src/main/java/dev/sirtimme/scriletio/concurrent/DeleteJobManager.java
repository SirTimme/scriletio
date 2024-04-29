package dev.sirtimme.scriletio.concurrent;

import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.*;

public class DeleteJobManager {
	private final ConcurrentMap<Long, ScheduledFuture<?>> pendingTasks;
	private final ScheduledExecutorService executorService;

	public DeleteJobManager() {
		this.pendingTasks = new ConcurrentHashMap<>();
		this.executorService = Executors.newScheduledThreadPool(5);
	}

	public void cancelJob(final long jobId) {
		final var deleteJob = pendingTasks.get(jobId);

		if (deleteJob != null) {
			deleteJob.cancel(true);
			pendingTasks.remove(jobId);
		}
	}

	public void submitJob(final long jobId, final Message message, final long duration) {
		final var deleteMessageJob = new Runnable() {
			@Override
			public void run() {
				message.delete().queue();
				pendingTasks.remove(jobId);
			}
		};

		final var future = executorService.schedule(deleteMessageJob, duration, TimeUnit.MINUTES);
		pendingTasks.put(jobId, future);
	}
}
