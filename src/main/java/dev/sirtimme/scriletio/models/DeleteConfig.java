package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "delete_configs", indexes = @Index(name = "idk_delete_config_author", columnList = "author_id"))
public class DeleteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NaturalId
    @Column(name = "author_id", nullable = false)
    private long authorId;

    @Column(name = "guild_id", nullable = false)
    private long guildId;

    @Column(name = "channel_id", nullable = false)
    private long channelId;

    @Column(name = "duration", nullable = false)
    private long duration;

    public DeleteConfig() {
    }

    public DeleteConfig(final long authorId, final long guildId, final long channelId, final long duration) {
        this.authorId = authorId;
        this.guildId = guildId;
        this.channelId = channelId;
        this.duration = duration;
    }
}