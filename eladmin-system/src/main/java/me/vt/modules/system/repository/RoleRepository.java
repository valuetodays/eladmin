package me.vt.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.system.domain.Role;

/**
 * @author Zheng Jie
 * @since 2018-12-03
 */
@ApplicationScoped
public class RoleRepository extends MyPanacheRepository<Role> {

    /**
     * 根据名称查询
     * @param name /
     * @return /
     */
    public Role findByName(String name) {
        return find("name = ?1", name).firstResult();
    }

}
