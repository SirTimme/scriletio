package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "agreements", indexes = @Index(name = "idx_user_id", unique = true, columnList = "user_id"))
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NaturalId
    @Column(name = "user_id", nullable = false)
    private long userId;

    // needed for Hibernate
    public Agreement() {
    }

    public Agreement(final long userId) {
        this.userId = userId;
    }
}