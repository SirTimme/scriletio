package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

public class UserRepository implements IRepository<User> {
	private final EntityManager entityManager;

	public UserRepository(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void add(final User entity) {
		entityManager.persist(entity);
	}

	@Override
	public User get(final long id) {
		return entityManager
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
		entityManager.remove(entity);
	}
}