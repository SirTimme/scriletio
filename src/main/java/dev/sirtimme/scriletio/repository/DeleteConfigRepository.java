package dev.sirtimme.scriletio.repository;

import dev.sirtimme.iuvo.repository.QueryableRepository;
import dev.sirtimme.scriletio.entities.DeleteConfig;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

public class DeleteConfigRepository extends QueryableRepository<DeleteConfig> {
    private final EntityManager context;

    public DeleteConfigRepository(final EntityManager context) {
        super(DeleteConfig.class, context);
        this.context = context;
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
}