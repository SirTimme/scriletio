package dev.sirtimme.scriletio.repositories;

public interface IRepository<T> {
	void add(final T entity);

	T get(final long id);

	void delete(final T entity);
}