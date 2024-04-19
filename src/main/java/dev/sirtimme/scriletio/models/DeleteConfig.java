package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;

@Entity
@Table(name = "delete_configs")
public class DeleteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "guild_id", nullable = false)
    private long guildId;

    @Column(name = "channel_id", nullable = false)
    private long channelId;

    @Column(name = "duration", nullable = false)
    private long duration;

    public DeleteConfig() {
    }

    public DeleteConfig(final long guildId, final long channelId, final long duration) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.duration = duration;
    }
}