package dev.sirtimme.scriletio.entities;

import dev.sirtimme.iuvo.api.entity.IEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_id", unique = true, columnList = "user_id"))
public class User implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NaturalId
    @Column(name = "user_id", nullable = false)
    private long userId;

    // needed for Hibernate
    public User() {
    }

    public User(final long userId) {
        this.userId = userId;
    }
}