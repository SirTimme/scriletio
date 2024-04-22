package dev.sirtimme.scriletio.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_id", unique = true, columnList = "user_id"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NaturalId
    @Column(name = "user_id", nullable = false)
    private long userId;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<DeleteConfig> configs;

    public User() {
    }

    public User(final long userId, final List<DeleteConfig> configs) {
        this.userId = userId;
        this.configs = configs;
    }

    public void addConfig(final DeleteConfig created) {
        this.configs.add(created);
    }

    public void removeConfig(final DeleteConfig removed) {
        this.configs.remove(removed);
    }

    public List<DeleteConfig> getConfigs() {
        return this.configs;
    }
}