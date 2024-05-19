package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.DeleteConfig;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

public class DeleteConfigRepository implements IRepository<DeleteConfig> {
    private final EntityManager context;

    public DeleteConfigRepository(final EntityManager context) {
        this.context = context;
    }

    @Override
    public void add(final DeleteConfig entity) {
        context.persist(entity);
    }

    @Override
    public DeleteConfig get(final long id) {
        return context.unwrap(Session.class).bySimpleNaturalId(DeleteConfig.class).load(id);
    }

    @Override
    public List<DeleteConfig> findAll(final long id) {
        return context
            .unwrap(Session.class)
            .createNamedQuery("DeleteConfig_findByGuildId", DeleteConfig.class)
            .setParameter("guildId", id)
            .list();
    }

    @Override
    public void delete(final DeleteConfig entity) {
        context.remove(entity);
    }
}