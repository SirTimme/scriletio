package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.entities.DeleteConfig;
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
            .createSelectionQuery("FROM DeleteConfig WHERE guildId = :guildId", DeleteConfig.class)
            .setParameter("guildId", id)
            .list();
    }

    @Override
    public void deleteAll(final long id) {
        context
            .unwrap(Session.class)
            .createMutationQuery("DELETE FROM DeleteConfig WHERE authorId = :authorId")
            .setParameter("authorId", id)
            .executeUpdate();
    }

    @Override
    public void delete(final DeleteConfig entity) {
        context.remove(entity);
    }
}