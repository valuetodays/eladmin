package me.zhengjie;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
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

    public Entity save(Entity entity) {
        persist(entity);
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
