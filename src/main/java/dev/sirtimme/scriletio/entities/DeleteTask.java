package dev.sirtimme.scriletio.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "delete_tasks")
public class DeleteTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private DeleteConfig deleteConfig;

    @NaturalId
    @Column(name = "message_id", nullable = false)
    private long messageId;

    @Column(name = "deleted_at", nullable = false)
    private Timestamp deletedAt;

    // needed for Hibernate
    public DeleteTask() {
    }

    public DeleteTask(final DeleteConfig deleteConfig, final long messageId, final Timestamp deletedAt) {
        this.deleteConfig = deleteConfig;
        this.messageId = messageId;
        this.deletedAt = deletedAt;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public Timestamp getDeletedAt() {
        return this.deletedAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof final DeleteTask that)) {
            return false;
        }

        return id == that.id && messageId == that.messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageId);
    }
}