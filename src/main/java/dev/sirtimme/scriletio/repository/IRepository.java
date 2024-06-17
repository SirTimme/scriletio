package dev.sirtimme.scriletio.repository;

import dev.sirtimme.scriletio.entities.IEntity;

public interface IRepository<T extends IEntity> {
    void add(final T entity);

    T get(final long id);

    void delete(final T entity);
}