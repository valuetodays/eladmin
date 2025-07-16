
package me.zhengjie.modules.system.repository;

import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@ApplicationScoped
public class RoleRepository extends MyPanacheRepository<Role> {

    /**
     * 根据名称查询
     * @param name /
     * @return /
     */
    public Role findByName(String name) {

    }

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    @Query(value = "SELECT r.* FROM sys_role r, sys_users_roles u WHERE " +
            "r.role_id = u.role_id AND u.user_id = ?1",nativeQuery = true)
    public Set<Role> findByUserId(Long id) {

    }

    /**
     * 解绑角色菜单
     * @param id 菜单ID
     */
    @Modifying
    @Query(value = "delete from sys_roles_menus where menu_id = ?1",nativeQuery = true)
    public void untiedMenu(Long id) {

    }

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = "select count(1) from sys_role r, sys_roles_depts d where " +
            "r.role_id = d.role_id and d.dept_id in ?1",nativeQuery = true)
    public int countByDepts(Set<Long> deptIds) {

    }

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    @Query(value = "SELECT r.* FROM sys_role r, sys_roles_menus m WHERE " +
            "r.role_id = m.role_id AND m.menu_id in ?1",nativeQuery = true)
    public List<Role> findInMenuId(List<Long> menuIds) {

    }
}
