package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.DeleteConfig;
import jakarta.persistence.EntityManager;

public class DeleteConfigRepository extends Repository<DeleteConfig> {
	public DeleteConfigRepository(final EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected Class<DeleteConfig> getEntityClass() {
		return DeleteConfig.class;
	}
}