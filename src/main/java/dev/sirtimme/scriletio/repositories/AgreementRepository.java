package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.Agreement;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

public class AgreementRepository implements IRepository<Agreement> {
    private final EntityManager context;

    public AgreementRepository(final EntityManager context) {
        this.context = context;
    }

    @Override
    public void add(final Agreement entity) {
        context.persist(entity);
    }

    @Override
    public Agreement get(final long id) {
        return context
            .unwrap(Session.class)
            .bySimpleNaturalId(Agreement.class)
            .load(id);
    }

    @Override
    public List<Agreement> findAll(final long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(final Agreement entity) {
        context.remove(entity);
    }
}