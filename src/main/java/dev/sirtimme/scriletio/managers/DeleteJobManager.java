package dev.sirtimme.scriletio.managers;

import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.*;

public class DeleteJobManager {
	private final ConcurrentMap<Long, ScheduledFuture<?>> pendingJobs;
	private final ScheduledExecutorService executorService;

	public DeleteJobManager() {
		this.pendingJobs = new ConcurrentHashMap<>();
		this.executorService = Executors.newScheduledThreadPool(5);
	}

	public void cancelJob(final long jobId) {
		final var deleteJob = pendingJobs.get(jobId);

		if (deleteJob != null) {
			deleteJob.cancel(true);
			pendingJobs.remove(jobId);
		}
	}

	public void submitJob(final long jobId, final Message message, final long duration) {
		final var deleteJob = new Runnable() {
			@Override
			public void run() {
				message.delete().queue();
				pendingJobs.remove(jobId);
			}
		};

		final var scheduledJob = executorService.schedule(deleteJob, duration, TimeUnit.MINUTES);
		pendingJobs.put(jobId, scheduledJob);
	}
}