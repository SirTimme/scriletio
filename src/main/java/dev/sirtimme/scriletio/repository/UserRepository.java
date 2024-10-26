package dev.sirtimme.scriletio.repository;

import dev.sirtimme.iuvo.api.repository.Repository;
import dev.sirtimme.scriletio.entities.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends Repository<User> {
    public UserRepository(final EntityManager context) {
        super(User.class, context);
    }
}