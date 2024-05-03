package dev.sirtimme.scriletio.repositories;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public abstract class Repository<T> implements IRepository<T> {
	private final EntityManager entityManager;

	protected Repository(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void add(final T entity) {
		entityManager.persist(entity);
	}

	@Override
	public T get(final long id) {
		return entityManager.unwrap(Session.class).bySimpleNaturalId(getEntityClass()).load(id);
	}

	@Override
	public void delete(final T entity) {
		entityManager.remove(entity);
	}

	protected abstract Class<T> getEntityClass();
}