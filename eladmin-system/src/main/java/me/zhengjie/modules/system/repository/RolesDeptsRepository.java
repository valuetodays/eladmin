package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.RolesDepts;

import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@ApplicationScoped
public class RolesDeptsRepository extends MyPanacheRepository<RolesDepts> {
    public List<RolesDepts> findByRoleIds(List<Long> roleIds) {
        return find("roleId in ?1", roleIds).list();
    }
}
