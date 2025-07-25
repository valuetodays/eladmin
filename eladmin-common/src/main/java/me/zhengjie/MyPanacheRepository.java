package me.zhengjie;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-16
 */
public class MyPanacheRepository<Entity> implements PanacheRepository<Entity> {

    @Inject
    EntityManager em;

    public Entity saveOrUpdate(Entity entity) {
        PersistenceUnitUtil util = em.getEntityManagerFactory().getPersistenceUnitUtil();
        boolean hasId = util.getIdentifier(entity) != null;
        if (hasId) {
            return update(entity);
        }
        return save(entity);
    }

    public Entity save(Entity entity) {
        persist(entity);
        return entity;
    }

    public Entity update(Entity entity) {
        em.merge(entity);
        return entity;
    }
    @Transactional
    public long deleteAllByIdIn(Set<Long> ids) {
        return delete(" where id in ?1", ids);
    }

    public List<Entity> findAllById(Collection<Long> ids) {
        return find("id in ?1", ids).list();
    }

    public List<Entity> findAllByIds(Collection<Long> ids) {
        return findAllById(ids);
    }

    public List<Entity> findByIds(Collection<Long> ids) {
        return findAllById(ids);
    }
}
