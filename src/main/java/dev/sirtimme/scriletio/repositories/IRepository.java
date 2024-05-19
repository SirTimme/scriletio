package dev.sirtimme.scriletio.repositories;

import java.util.List;

public interface IRepository<T> {
    void add(final T entity);

    T get(final long id);

    List<T> findAll(final long id);

    void delete(final T entity);
}