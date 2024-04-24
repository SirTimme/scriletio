package dev.sirtimme.scriletio.repositories;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;

public abstract class Repository<T> implements IRepository<T> {
	private final EntityManagerFactory entityManagerFactory;

	protected Repository(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void add(final T entity) {
		final var entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public T get(final long id) {
		final var entityManager = entityManagerFactory.createEntityManager();
		final var result = entityManager.unwrap(Session.class).bySimpleNaturalId(getEntityClass()).load(id);
		entityManager.close();
		return result;
	}

	@Override
	public void update(final T entity) {
		final var entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.merge(entity);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public void delete(final T entity) {
		final var entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(entity);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	protected abstract Class<T> getEntityClass();
}