package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_id", unique = true, columnList = "user_id"))
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NaturalId
	@Column(name = "user_id", nullable = false)
	private long userId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<DeleteConfig> configs;

	// needed for Hibernate
	public User() {
	}

	public User(final long userId, final List<DeleteConfig> configs) {
		this.userId = userId;
		this.configs = configs;
	}

	public void addConfig(final DeleteConfig created) {
		this.configs.add(created);
	}

	public void removeConfig(final long channelId) {
		final var toBeRemoved = this.configs.stream().filter(config -> config.getChannelId() == channelId).findFirst().orElse(null);
		this.configs.remove(toBeRemoved);
	}

	public List<DeleteConfig> getConfigs() {
		return this.configs;
	}
}