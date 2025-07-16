package me.zhengjie;

import java.util.Set;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

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
    public void deleteAllByIdIn(Set<Long> ids) {
        delete(" ids in ?1", ids);
    }
}
