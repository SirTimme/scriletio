package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends Repository<User> {
	public UserRepository(final EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}
}