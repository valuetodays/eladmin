package me.zhengjie.modules.system.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.User;

/**
 * @author Zheng Jie
 * @since 2018-11-22
 */
@ApplicationScoped
public class UserRepository extends MyPanacheRepository<User> {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return /
     */
    public User findByUsername(String username) {
        return find("username = ?1", username).firstResult();
    }

    /**
     * 根据邮箱查询
     * @param email 邮箱
     * @return /
     */
    public User findByEmail(String email) {
        return find("email = ?1", email).firstResult();
    }

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return /
     */
    public User findByPhone(String phone) {
        return find("phone = ?1", phone).firstResult();
    }

    /**
     * 修改密码
     * @param username 用户名
     * @param pass 密码
     * @param lastPasswordResetTime /
     */
    @Transactional
//    @Query(value = "update sys_user set password = ?2 , pwd_reset_time = ?3 where username = ?1",nativeQuery = true)
    public void updatePass(String username, String pass, Date lastPasswordResetTime) {
        update("set password = ?2 , pwdResetTime = ?3  where username = ?1", username, pass, lastPasswordResetTime);
    }

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    @Transactional
//    @Query(value = "update sys_user set email = ?2 where username = ?1",nativeQuery = true)
    public void updateEmail(String username, String email) {
        update("set email = ?2 where username = ?1", username, email);
    }

    /**
     * 根据角色查询用户
     * @param roleId /
     * @return /
     */
//    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r WHERE" +
//            " u.user_id = r.user_id AND r.role_id = ?1", nativeQuery = true)
    public List<User> findByRoleId(Long roleId) {
        return null;
    }

    /**
     * 根据角色中的部门查询
     * @param deptId /
     * @return /
     */
//    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE " +
//            "u.user_id = r.user_id AND r.role_id = d.role_id AND d.dept_id = ?1 group by u.user_id", nativeQuery = true)
    public List<User> findByRoleDeptId(Long deptId) {
        return null;
    }

    /**
     * 根据菜单查询
     * @param id 菜单ID
     * @return /
     */
//    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles ur, sys_roles_menus rm WHERE\n" +
//            "u.user_id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = ?1 group by u.user_id", nativeQuery = true)
    public List<User> findByMenuId(Long id) {
        return null;
    }


    /**
     * 根据岗位查询
     * @param ids /
     * @return /
     */
//    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.user_id = j.user_id AND j.job_id IN ?1", nativeQuery = true)
    public int countByJobs(Set<Long> ids) {
        return -1;
    }

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
//    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id IN ?1", nativeQuery = true)
    public int countByDepts(Set<Long> deptIds) {
        return -1;
    }

    /**
     * 根据角色查询
     * @param ids /
     * @return /
     */
//    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE " +
//            "u.user_id = r.user_id AND r.role_id in ?1", nativeQuery = true)
    public int countByRoles(Set<Long> ids) {
        return -1;
    }

    /**
     * 重置密码
     * @param ids 、
     * @param pwd 、
     */
    @Transactional
//    @Query(value = "update sys_user set password = ?2 where user_id in ?1",nativeQuery = true)
    public void resetPwd(Set<Long> ids, String pwd) {
        update("set password = ?2 where userId in ?1", pwd, ids);
    }
}
