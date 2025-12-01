package me.vt.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.List;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.system.domain.UsersRole;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@ApplicationScoped
public class UsersRoleRepository extends MyPanacheRepository<UsersRole> {
    public List<UsersRole> findByUserId(Long userId) {
        return find("userId = ?1", userId).list();
    }

    public List<UsersRole> findByRoleIds(Collection<Long> roleIds) {
        return find("roleId in ?1", roleIds).list();
    }
}
