package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "delete_configs", indexes = {
    @Index(name = "idx_config_channel_id", unique = true, columnList = "channel_id"),
    @Index(name = "idx_config_guild_id", columnList = "guild_id"),
    @Index(name = "idx_config_author_id", columnList = "author_id")
})
public class DeleteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_id", nullable = false)
    private long authorId;

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

    public DeleteConfig(final long authorId, final long guildId, final long channelId, final long duration) {
        this.authorId = authorId;
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

    public long getId() {
        return this.id;
    }
}