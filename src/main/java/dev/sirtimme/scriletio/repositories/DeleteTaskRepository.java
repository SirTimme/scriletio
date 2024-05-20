package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.DeleteTask;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

public class DeleteTaskRepository implements IRepository<DeleteTask> {
    private final EntityManager context;

    public DeleteTaskRepository(final EntityManager context) {
        this.context = context;
    }

    @Override
    public void add(final DeleteTask entity) {
        context.persist(entity);
    }

    @Override
    public DeleteTask get(final long id) {
        return context
            .unwrap(Session.class)
            .bySimpleNaturalId(DeleteTask.class)
            .load(id);
    }

    @Override
    public List<DeleteTask> findAll(final long id) {
        return context
            .unwrap(Session.class)
            .createNamedQuery("DeleteTask_findByChannelId", DeleteTask.class)
            .setParameter("channelId", id)
            .list();
    }

    @Override
    public void delete(final DeleteTask entity) {
        context.remove(entity);
    }
}