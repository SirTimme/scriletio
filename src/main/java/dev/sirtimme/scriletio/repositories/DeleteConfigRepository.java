package dev.sirtimme.scriletio.repositories;

import dev.sirtimme.scriletio.models.DeleteConfig;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.util.List;

public class DeleteConfigRepository implements IRepository<DeleteConfig> {
	private final EntityManager entityManager;

	public DeleteConfigRepository(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void add(final DeleteConfig entity) {
		entityManager.persist(entity);
	}

	@Override
	public DeleteConfig get(final long id) {
		return entityManager.unwrap(Session.class).bySimpleNaturalId(DeleteConfig.class).load(id);
	}

	@Override
	public List<DeleteConfig> findAll(final long id) {
		return entityManager
				.unwrap(Session.class)
				.createNamedQuery("DeleteConfig_findByGuildId", DeleteConfig.class)
				.setParameter("guildId", id)
				.list();
	}

	@Override
	public void delete(final DeleteConfig entity) {
		entityManager.remove(entity);
	}
}