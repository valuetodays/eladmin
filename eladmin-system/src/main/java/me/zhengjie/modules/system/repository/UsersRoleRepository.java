package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.UsersRole;

import java.util.List;

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
}
