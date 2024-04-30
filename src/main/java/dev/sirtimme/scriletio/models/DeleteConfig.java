package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "delete_configs", indexes = @Index(name = "idx_channel_id", unique = true, columnList = "channel_id"))
public class DeleteConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private User user;

	@NaturalId
	@Column(name = "channel_id", nullable = false)
	private long channelId;

	@Column(name = "duration", nullable = false)
	private long duration;

	// needed for Hibernate
	public DeleteConfig() {
	}

	public DeleteConfig(final User user, final long channelId, final long duration) {
		this.user = user;
		this.channelId = channelId;
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(final long duration) {
		this.duration = duration;
	}

	public long getChannelId() {
		return channelId;
	}
}