package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "delete_configs", indexes = { @Index(name = "idx_channel_id", unique = true, columnList = "channel_id"), @Index(name = "idx_guild_id", columnList = "guild_id") })
@NamedQuery(name = "DeleteConfig_findByGuildId", query = "FROM DeleteConfig WHERE guildId = :guildId")
public class DeleteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @Column(name = "guild_id", nullable = false)
    private long guildId;

    @NaturalId
    @Column(name = "channel_id", nullable = false)
    private long channelId;

    @Column(name = "duration", nullable = false)
    private long duration;

    // needed for Hibernate
    public DeleteConfig() {
    }

    public DeleteConfig(final User user, final long guildId, final long channelId, final long duration) {
        this.user = user;
        this.guildId = guildId;
        this.channelId = channelId;
        this.duration = duration;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    public long getChannelId() {
        return this.channelId;
    }

    public User getUser() {
        return this.user;
    }

    public long getId() {
        return this.id;
    }
}