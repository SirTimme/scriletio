package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.sql.Timestamp;

@Entity
@Table(name = "delete_tasks", indexes = @Index(name = "idx_task_channel_id", columnList = "channel_id"))
@NamedQuery(name = "DeleteTask_findByChannelId", query = "FROM DeleteTask WHERE channelId = :channelId")
public class DeleteTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "channel_id", nullable = false)
    private long channelId;

    @NaturalId
    @Column(name = "message_id", nullable = false)
    private long messageId;

    @Column(name = "deleted_at", nullable = false)
    private Timestamp deletedAt;

    // needed for Hibernate
    public DeleteTask() {
    }

    public DeleteTask(final long channelId, final long messageId, final Timestamp deletedAt) {
        this.channelId = channelId;
        this.messageId = messageId;
        this.deletedAt = deletedAt;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public Timestamp getDeletedAt() {
        return this.deletedAt;
    }
}