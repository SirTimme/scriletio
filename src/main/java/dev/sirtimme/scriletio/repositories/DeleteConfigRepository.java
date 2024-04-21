package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.DeleteConfig;
import jakarta.persistence.EntityManagerFactory;

public class DeleteConfigRepository extends Repository<DeleteConfig> {
    public DeleteConfigRepository(final EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    protected Class<DeleteConfig> getEntityClass() {
        return DeleteConfig.class;
    }
}