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

    public List<RolesMenus> findByMenuIds(Collection<Long> menuIds) {
        return find("menuId in ?1", menuIds).list();
    }

    public void deleteByRoleId(Long roleId) {
        delete("roleId = ?1", roleId);
    }

    public void deleteByMenuId(Long menuId) {
        delete("menuId = ?1", menuId);
    }
}
