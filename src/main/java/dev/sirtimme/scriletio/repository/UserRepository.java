package dev.sirtimme.scriletio.repository;

import dev.sirtimme.scriletio.entities.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class UserRepository implements IRepository<User> {
    private final EntityManager context;

    public UserRepository(final EntityManager context) {
        this.context = context;
    }

    @Override
    public void add(final User entity) {
        context.persist(entity);
    }

    @Override
    public User get(final long id) {
        return context
            .unwrap(Session.class)
            .bySimpleNaturalId(User.class)
            .load(id);
    }

    @Override
    public void delete(final User entity) {
        context.remove(entity);
    }
}