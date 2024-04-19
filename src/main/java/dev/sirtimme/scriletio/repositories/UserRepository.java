package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.User;
import jakarta.persistence.EntityManagerFactory;

public class UserRepository extends Repository<User> {
    public UserRepository(final EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}