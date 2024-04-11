package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;

@Entity
@Table(name = "delete_configs", indexes = @Index(name = "idk_delete_config_author", columnList = "author_id"))
public class AutoDeleteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_id", nullable = false)
    private long authorId;

    @Column(name = "guild_id", nullable = false)
    private long guildId;

    @Column(name = "channel_id", nullable = false)
    private long channelId;

    @Column(name = "message_id", nullable = false)
    private long messageId;
}