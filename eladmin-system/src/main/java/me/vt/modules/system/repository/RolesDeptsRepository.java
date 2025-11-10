package me.vt.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.system.domain.RolesDepts;

import java.util.Collection;
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

    public List<RolesDepts> findByDeptIds(Collection<Long> deptIds) {
        return find("deptId in ?1", deptIds).list();
    }
}
