package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.RolesMenus;

import java.util.Collection;
import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@ApplicationScoped
public class RolesMenusRepository extends MyPanacheRepository<RolesMenus> {
    public List<RolesMenus> findByRoleIds(Collection<Long> roleIds) {
        return find("roleId in ?1", roleIds).list();
    }
}
