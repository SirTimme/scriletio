package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Entity
@Table(name = "delete_configs", indexes = @Index(name = "idx_channel_id", unique = true, columnList = "channel_id"))
public class DeleteConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NaturalId
	@Column(name = "channel_id", nullable = false)
	private long channelId;

	@Column(name = "duration", nullable = false)
	private long duration;

	public DeleteConfig() {
	}

	public DeleteConfig(final long channelId, final long duration) {
		this.channelId = channelId;
		this.duration = duration;
	}

	public long getDuration() {
		return this.duration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(channelId);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final DeleteConfig that = (DeleteConfig) o;
		return channelId == that.channelId;
	}
}