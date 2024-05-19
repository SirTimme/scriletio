package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

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
    public List<User> findAll(final long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(final User entity) {
        context.remove(entity);
    }
}