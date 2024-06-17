package dev.sirtimme.scriletio.repository;

import dev.sirtimme.scriletio.entities.IEntity;

import java.util.List;

public interface IQueryableRepository<T extends IEntity> extends IRepository<T> {
    List<T> findAll(final long id);

    void deleteAll(final long id);
}
