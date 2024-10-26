package dev.sirtimme.scriletio.entities;

import dev.sirtimme.iuvo.api.entity.IEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "delete_configs", indexes = {
    @Index(name = "idx_config_channel_id", unique = true, columnList = "channel_id"),
    @Index(name = "idx_config_guild_id", columnList = "guild_id"),
    @Index(name = "idx_config_author_id", columnList = "author_id")
})
public class DeleteConfig implements IEntity {
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

    @OneToMany(mappedBy = "deleteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<DeleteTask> deleteTasks;

    @Column(name = "duration", nullable = false)
    private long duration;

    // needed for Hibernate
    public DeleteConfig() {
    }

    public DeleteConfig(final long authorId, final long guildId, final long channelId, final List<DeleteTask> deleteTasks, final long duration) {
        this.authorId = authorId;
        this.guildId = guildId;
        this.channelId = channelId;
        this.deleteTasks = deleteTasks;
        this.duration = duration;
    }

    public DeleteTask getTask(final long messageId) {
        return this.deleteTasks.stream().filter(task -> task.getMessageId() == messageId).findFirst().orElse(null);
    }

    public List<DeleteTask> getDeleteTasks() {
        return this.deleteTasks;
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
}