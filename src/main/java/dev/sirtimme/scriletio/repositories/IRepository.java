package dev.sirtimme.scriletio.repositories;

public interface IRepository<T> {
	void add(final T entity);

	T get(final long id);

	void update(final T entity);

	void delete(final long id);
}